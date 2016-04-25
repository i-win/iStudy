package com.iwin.istudy.engine;

import com.iwin.istudy.R;
import com.iwin.istudy.entity.Pet;
import com.iwin.istudy.interfaces.PetAttributes;

import java.util.Random;

/**
 * Created by sxq on 2016/4/24.
 */
public class SquirrelPet implements PetAttributes {

    /**
     * 松鼠小贱的所有动作
     */
    private static final int[] ACTIONS = {
            R.drawable.squirrel01,R.drawable.squirrel02,R.drawable.squirrel03,R.drawable.squirrel04,
            R.drawable.squirrel05,R.drawable.squirrel06,R.drawable.squirrel07,R.drawable.squirrel08,
            R.drawable.squirrel09,R.drawable.squirrel10,R.drawable.squirrel11,R.drawable.squirrel12,
            R.drawable.squirrel13,R.drawable.squirrel14,R.drawable.squirrel15,R.drawable.squirrel16,
            R.drawable.squirrel17,R.drawable.squirrel18,R.drawable.squirrel19,R.drawable.squirrel20,
            R.drawable.squirrel21,R.drawable.squirrel22,R.drawable.squirrel23,R.drawable.squirrel24,
            R.drawable.squirrel25,R.drawable.squirrel26,R.drawable.squirrel27,R.drawable.squirrel28,
            R.drawable.squirrel29,R.drawable.squirrel30,R.drawable.squirrel31,R.drawable.squirrel32,
            R.drawable.squirrel33,R.drawable.squirrel34,R.drawable.squirrel35,R.drawable.squirrel36,
            R.drawable.squirrel37,R.drawable.squirrel38,R.drawable.squirrel39,R.drawable.squirrel40,
            R.drawable.squirrel41,R.drawable.squirrel42,R.drawable.squirrel43,R.drawable.squirrel44,
            R.drawable.squirrel45,R.drawable.squirrel46,R.drawable.squirrel47,R.drawable.squirrel48,
            R.drawable.squirrel49,R.drawable.squirrel50,R.drawable.squirrel51,R.drawable.squirrel52,
            R.drawable.squirrel53,R.drawable.squirrel54,R.drawable.squirrel55,R.drawable.squirrel56,
            R.drawable.squirrel57,R.drawable.squirrel58,R.drawable.squirrel59,R.drawable.squirrel60,
    };
    /**
     * 宠物动作总数
     */
    private int actionSum = ACTIONS.length;

    @Override
    public int getRandomAction() {
        Random random = new Random();
        int res = random.nextInt(actionSum);
        return ACTIONS[res];
    }

    @Override
    public String getRandomSaying() {
        return "";
    }

    @Override
    public Pet getDeauft() {
        Pet pet = new Pet();
        pet.setAction(ACTIONS[1]);
        pet.setSaying("");
        return pet;
    }

    @Override
    public Pet getStudy() {
        Pet pet = new Pet();
        Random random = new Random();
        int action = random.nextInt(3);
        switch (action){
            case 0:
                pet.setAction(ACTIONS[1]);
                pet.setSaying("小伙子~~好好学，老夫看好你，非系统App老夫就帮你禁掉了");
                break;
            case 1:
                pet.setAction(ACTIONS[35]);
                pet.setSaying("你就放心去学习吧~~非系统App交给老夫保管准没错");
                break;
            case 2:
                pet.setAction(ACTIONS[55]);
                pet.setSaying("老夫已经收到你的愿望了，放心，非系统App在老夫这边很安全");
                break;
            default:
                pet.setAction(ACTIONS[1]);
                pet.setSaying("");
                break;
        }
        return pet;
    }

    @Override
    public Pet getRest() {
        Pet pet = new Pet();
        pet.setAction(ACTIONS[1]);
        pet.setSaying("老夫抽根烟先~~别偷偷玩手机");
        return pet;
    }

    @Override
    public Pet getWarning() {
        Pet pet = new Pet();
        Random random = new Random();
        int action = random.nextInt(5);
        switch (action){
            case 0:
                pet.setAction(ACTIONS[45]);
                pet.setSaying("啥~~你不是跟老夫说不玩手机了么，咋转眼就又来玩了");
                break;
            case 1:
                pet.setAction(ACTIONS[17]);
                pet.setSaying("让你再玩~~等老夫扣撮出来弹死你");
                break;
            case 2:
                pet.setAction(ACTIONS[06]);
                pet.setSaying("孺子不可教也~非要气死老夫");
                break;
            case 3:
                pet.setAction(ACTIONS[52]);
                pet.setSaying("看老夫的卖萌绝招~~别玩了好嘛~");
                break;
            case 4:
                pet.setAction(ACTIONS[55]);
                pet.setSaying("老夫说过非系统App尽管放心交给老夫保管");
                break;
            default:
                pet.setAction(ACTIONS[1]);
                pet.setSaying("");
                break;
        }
        return pet;
    }

    @Override
    public Pet getFinish() {
        Pet pet = new Pet();
        Random random = new Random();
        int action = random.nextInt(2);
        switch (action){
            case 0:
                pet.setAction(ACTIONS[1]);
                pet.setSaying("小伙子表现不错~老夫已经解封App了，玩去吧");
                break;
            case 1:
                pet.setAction(ACTIONS[26]);
                pet.setSaying("Good~");
                break;
            default:
                pet.setAction(ACTIONS[1]);
                pet.setSaying("");
                break;
        }
        return pet;
    }
}
