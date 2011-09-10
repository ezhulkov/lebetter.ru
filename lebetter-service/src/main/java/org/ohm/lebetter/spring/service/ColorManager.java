package org.ohm.lebetter.spring.service;

import org.ohm.lebetter.model.impl.entities.ColorEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.service.GenericManager;

import java.util.List;


public interface ColorManager extends GenericManager<ColorEntity, UserEntity> {

    public List<ColorEntity> getLikeColors(ColorEntity color);

    public List<ColorEntity> getUIColors();

    public ColorEntity getColorByCode(String code);

    public ColorEntity getColorByNameIgnoreCase(String color);
}