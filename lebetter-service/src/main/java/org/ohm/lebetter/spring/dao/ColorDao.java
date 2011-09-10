package org.ohm.lebetter.spring.dao;

import org.ohm.lebetter.model.impl.entities.ColorEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.dao.GenericDao;

import java.util.List;


public interface ColorDao extends GenericDao<ColorEntity, UserEntity> {

    public List<ColorEntity> getLikeColors(ColorEntity color);

    public List<ColorEntity> getUIColors();

    public ColorEntity getColorByCode(String code);

    public boolean existsByName(String name, Long id);

}