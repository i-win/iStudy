package com.iwin.istudy.interfaces;

import com.iwin.istudy.entity.Pet;

/**
 * Created by sxq on 2016/4/24.
 */
public interface PetAttributes {
    /**
     * 获取宠物随机动作
     */
    public int getRandomAction();

    public String getRandomSaying();

    /**
     * 获取默认的宠物动作，语录
     */
    public Pet getDeauft();

    /**
     * 获取学习的动作，语录
     */
    public Pet getStudy();

    /**
     * 获取休息的动作，语录
     */
    public Pet getRest();

    /**
     * 获取温馨提示的动作，语录
     */
    public Pet getWarning();

    /**
     * 获取学习完成的动作，语录
     */
    public Pet getFinish();

}
