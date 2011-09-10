package org.ohm.lebetter.spring.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.ProductDao;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.dao.impl.CreatorAwareDaoHibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductDaoHibernate
        extends CreatorAwareDaoHibernate<ProductEntity, UserEntity>
        implements ProductDao {

    public ProductDaoHibernate() {
        super(ProductEntity.class);
    }

    @Override
    public List<Long> getSearchProductIds(CategoryEntity pType,
                                          PropertyValueEntity[][] values,
                                          Order sort) {
        Projection projections = sort == null ? Projections.groupProperty("prod.rootId") :
                                 Projections.property("prod.rootId");
        return findProductsForSearch(projections, pType, values, sort, true);
    }

    private List findProductsForSearch(Projection projection,
                                       CategoryEntity category,
                                       PropertyValueEntity[][] values,
                                       Order sort, boolean promoSort) {

        if (category == null) {
            return new ArrayList();
        }

        DetachedCriteria criteria = DetachedCriteria.forClass(ProductEntity.class, "prod");
        criteria.setProjection(projection);
        criteria.add(getStandartFilter("prod"));
        criteria.add(Restrictions.eq("missing", false));
        criteria.createAlias("shop", "sh", CriteriaSpecification.INNER_JOIN).
                add(Restrictions.eq("sh.objectStatus", ObjectBaseEntity.Status.READY));
        //Only with good product type
        if (category.getParent() == null) {
            //add parent type as filter
            criteria.createAlias("categories", "cat", CriteriaSpecification.INNER_JOIN);
            criteria.createAlias("cat.parent", "rcat", CriteriaSpecification.INNER_JOIN);
            criteria.add(getStandartFilter("rcat"));
            criteria.add(Restrictions.eq("rcat.rootId", category.getRootId()));
        } else {
            //add type as filter
            criteria.createAlias("categories", "cat", CriteriaSpecification.INNER_JOIN);
            criteria.add(getStandartFilter("cat")).add(Restrictions.eq("cat.rootId", category.getRootId()));
        }

        //Set filters for every value
        List<Criterion> valFilters = new ArrayList<Criterion>();
        if (values != null) {
            for (PropertyValueEntity[] value : values) {
                if (value != null && value.length != 0 && value[0] != null) {

                    StringBuilder valSql = new StringBuilder();
                    valSql.append("{alias}.rootId in (");
                    valSql.append("select product_id from app_tag where ");
                    valSql.append("rootObject=true and objectDeleted=false and ");
                    valSql.append("type=0 and (");

                    List<Long> params = new ArrayList<Long>();
                    List<Type> types = new ArrayList<Type>();
                    for (int i = 0; i < value.length; i++) {
                        if (value[i] != null) {
                            valSql.append(" propertyvalue_id=? ");
                            if (i + 1 != value.length) {
                                valSql.append(" or ");
                            }
                            params.add(value[i].getRootId());
                            types.add(new LongType());
                        }
                    }

                    valSql.append("))");
                    Criterion valFilter = Restrictions.sqlRestriction(valSql.toString(),
                                                                      params.toArray(),
                                                                      types.toArray(new LongType[0]));
                    valFilters.add(valFilter);

                }
            }
        }
        for (Criterion valFilter : valFilters) {
            criteria.add(valFilter);
        }

        if (sort == null && promoSort) {
            criteria.createAlias("sh.shopCat", "sc", CriteriaSpecification.LEFT_JOIN);
            criteria.addOrder(OrderBySqlFormula.sqlFormula("max(coalesce({sc}.order_index,0)) desc"));
            criteria.addOrder(Order.desc("prod.rootId"));
        }

        //Set sort order
        if (sort != null) {
            criteria.addOrder(sort);
        }


        return criteria.getExecutableCriteria(getSession()).setCacheable(true).list();

    }

    public static class OrderBySqlFormula extends Order {
        public static final Pattern ALIAS_PATTERN = Pattern.compile("\\{([^\\}]+)\\}");
        private String sqlFormula;

        protected OrderBySqlFormula(String sqlFormula) {
            super(sqlFormula, true);
            this.sqlFormula = sqlFormula;
        }

        public String toString() {
            return sqlFormula;
        }

        public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
            StringBuffer sb = new StringBuffer(sqlFormula.length());
            Matcher matcher = ALIAS_PATTERN.matcher(sqlFormula);

            while (matcher.find()) {
                matcher.appendReplacement(sb, criteriaQuery.getSQLAlias(criteria, matcher.group(1) + "."));
            }
            matcher.appendTail(sb);
            return sb.toString();
        }

        public static Order sqlFormula(String sqlFormula) {
            return new OrderBySqlFormula(sqlFormula);
        }
    }

}
