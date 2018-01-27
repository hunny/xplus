# 行为型模式-策略模式【Strategy Pattern】

```java
/** 
 * 武器 --模板 
 */  
interface WeaponBehavior {  
    void useWeapon();  
}  
  
class KnifeBehavior implements WeaponBehavior {  
    @Override  
    public void useWeapon() {  
        System.out.println("实现用匕首刺杀...");  
    }  
}  
  
class BowAndArrowBehavior implements WeaponBehavior {  
    @Override  
    public void useWeapon() {  
        System.out.println("实现用弓箭设计...");  
    }  
}  
  
class AxeBehavior implements WeaponBehavior {  
    @Override  
    public void useWeapon() {  
        System.out.println("实现用斧头劈砍...");  
    }  
}  
  
class SwordBehavior implements WeaponBehavior {  
    @Override  
    public void useWeapon() {  
        System.out.println("实现用宝剑挥舞...");  
    }  
}  
  
/** 
 * 角色 
 */  
abstract class Character {  
    // 将接口作为抽象角色的Field以便封装  
    protected WeaponBehavior weaponBehavior;  
  
    public void setWeapon(WeaponBehavior w) {  
        weaponBehavior = w;  
    }  
  
    /** 
     * 这里有点类似“代理模式” 
     */  
    public void performWeapon() {  
        // do something...  
        weaponBehavior.useWeapon();  
        // do something...  
    }  
  
    public abstract void fight();  
}  
  
/** 
 * 国王使用宝剑挥舞 
 */  
class King extends Character {  
  
    public King() {  
        weaponBehavior = new SwordBehavior();  
    }  
  
    @Override  
    public void fight() {  
        System.out.println("国王使用宝剑挥舞...");  
    }  
  
}  
  
/** 
 * 皇后使用匕首刺杀 
 */  
class Queen extends Character {  
  
    public Queen() {  
        weaponBehavior = new KnifeBehavior();  
    }  
  
    @Override  
    public void fight() {  
        System.out.println("皇后使用匕首刺杀...");  
    }  
  
}  
  
/** 
 * Knight和Troll以此类推，这里就不写了 
 */  
  
/** 
 * 客户端测试 
 *  
 * @author Leo 
 */  
public class Test {  
    public static void main(String[] args) {  
        Character king = new King();  
        king.performWeapon();  
        // 这里有点类似于“状态模式”  
        king.setWeapon(new AxeBehavior());  
        king.performWeapon();  
  
        Character queen = new Queen();  
        queen.performWeapon();  
        queen.setWeapon(new BowAndArrowBehavior());  
        queen.performWeapon();  
    }  
}  
```