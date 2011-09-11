package org.ohm.lebetter.tapestry5.web.services.impl;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.ohm.lebetter.Constants.Roles;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.model.ObjectBaseEntity.Status;
import org.room13.mallcore.model.impl.entities.RoleEntity;
import org.room13.mallcore.spring.service.RoleManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenericStatusSelectModel extends GenericSelectModel<Status> {

    private static final Map<Status, Set<Status>> ALL_WORKFLOW =
            new HashMap<Status, Set<Status>>() {
                {
                    put(Status.NEW,
                        new HashSet<Status>() {{
                            add(Status.WAITING);
                        }});
                    put(Status.WAITING,
                        new HashSet<Status>() {{
                            add(Status.READY);
                            add(Status.REJECTED);
                        }});
                    put(Status.REJECTED,
                        new HashSet<Status>() {{
                            add(Status.WAITING);
                        }});
                    put(Status.READY,
                        new HashSet<Status>() {{
                            add(Status.REJECTED);
                            add(Status.WAITING);
                            add(Status.HIDE);
                        }});
                    put(Status.HIDE,
                        new HashSet<Status>() {{
                            add(Status.READY);
                        }});
                }
            };

    private static final Map<Status, Set<Status>> RESTRICTED_WORKFLOW =
            new HashMap<Status, Set<Status>>() {
                {
                    put(Status.NEW,
                        new HashSet<Status>() {{
                            add(Status.WAITING);
                        }});
                    put(Status.WAITING,
                        new HashSet<Status>() {{
                            add(Status.NEW);
                        }});
                    put(Status.REJECTED,
                        new HashSet<Status>() {{
                            add(Status.WAITING);
                        }});
                    put(Status.READY,
                        new HashSet<Status>() {{
                            add(Status.WAITING);
                            add(Status.HIDE);
                        }});
                    put(Status.HIDE,
                        new HashSet<Status>() {{
                            add(Status.READY);
                        }});
                }
            };

    private static final Map<String, Map<Status, Set<Status>>> GRAPH =
            new HashMap<String, Map<Status, Set<Status>>>() {
                {
                    put(Roles.ROLE_ADMIN, ALL_WORKFLOW);
                    put(Roles.ROLE_MANAGER, ALL_WORKFLOW);
                }
            };

    private Set<Status> getAllowedStatuses(ObjectBaseEntity object,
                                           List<RoleEntity> roles,
                                           Status currentStatus) {
        Set<Status> result = new HashSet<Status>();
        result.add(currentStatus);

        for (RoleEntity role : roles) {
            Map<Status, Set<Status>> set = GRAPH.get(role.getCode());
            if (set != null) {
                result.addAll(set.get(currentStatus));
            }
        }

        return result;
    }

    public GenericStatusSelectModel(UserEntity user, RoleManager rm,
                                    ObjectBaseEntity object,
                                    PropertyAccess propertyAccess) {

        super(Status.class);

        list = new HashSet<Status>();

        list.add(Status.NEW);
        list.add(Status.READY);

        listL = list;
        ClassPropertyAdapter adapter = propertyAccess.getAdapter(persistentClass.getClass());
        idFieldAdapter = adapter.getPropertyAdapter("id");
        labelFieldAdapter = adapter.getPropertyAdapter("name");
    }

    @Override
    public List<OptionModel> getOptions() {
        List<OptionModel> optionModelList = new ArrayList<OptionModel>();
        for (Status stat : list) {
            optionModelList.add(new OptionModelImpl(stat.toString(), stat.toString()));
        }
        return optionModelList;
    }

}
