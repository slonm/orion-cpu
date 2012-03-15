package ua.orion.tapestry.menu.lib;

import java.util.ArrayList;

/**
 * Class describes menu item position
 * @author Gennadiy Dobrovolsky
 */
public class MenuItemPosition implements Comparable<MenuItemPosition> {

    /**
     * Token to separate items in the menu path
     */
    public static final String token = ">";
    /**
     * Default menu item path
     */
    public static final String defaultUid = "Start";
    /**
     * Unique identifier of the menu item
     */
    public String uid = MenuItemPosition.defaultUid;
    /**
     * Position of the menu item as array
     */
    public WeightAndLabel[] positionSplitted;
    /**
     * Position of the menu item as String
     */
    public String position = MenuItemPosition.defaultUid;

    /**
     * Constructor
     * @param path строка, представляющая положение в дереве меню
     */
    public MenuItemPosition(String path) {
        String[] tmp = path.split(MenuItemPosition.token);
        int i, cnt = tmp.length;
        String num, lab;
        String getNumRegexp = "[^0-9].*$";
        String getLabRegexp = "^[0-9]*";
        positionSplitted = new WeightAndLabel[cnt];
        for (i = 0; i < cnt; i++) {
            num = tmp[i].replaceFirst(getNumRegexp, "");
            lab = tmp[i].replaceFirst(getLabRegexp, "");
            if (num.length() > 0) {
                positionSplitted[i] = new WeightAndLabel(lab, new Integer(num));
            } else {
                positionSplitted[i] = new WeightAndLabel(lab);
            }
        }
        createPosition();

        StringBuffer buf = new StringBuffer();
        lab="";
        for (WeightAndLabel tm : positionSplitted) {
            buf.append(lab + tm.label);
            lab=MenuItemPosition.token;
        }
        uid = buf.toString();
    }

    /**
     * Join positionSplitted in one string
     */
    private void createPosition() {
        StringBuffer buf = new StringBuffer();
        String lab="";
        for (WeightAndLabel tmp : positionSplitted) {
            buf.append(lab + tmp);
            lab=MenuItemPosition.token;
        }
        position = buf.toString();
    }

    /**
     * Update weight at position i
     * @param i position at which the new weight has to be set
     * @param weight new weight value
     */
    public void updatePositionWeight(int i, int weight) {
        if (i < positionSplitted.length) {
            positionSplitted[i].weight = weight;
            createPosition();
        }
    }


    /**
     * check if the current position
     * is child of the parent
     */
    public boolean isChildOf(MenuItemPosition parent) {
        // ---------- get uidStart - begin -----------------------------
        int i, cnt,parent_cnt;
        cnt = this.positionSplitted.length;
        parent_cnt=parent.positionSplitted.length;
        if(cnt<parent_cnt){
            return false;
        }

        for (i = 0; i < parent_cnt; i++) {
            if(!parent.positionSplitted[i].label.equalsIgnoreCase(this.positionSplitted[i].label)){
                return false;
            }
        }
        // ---------- get uidStart - end -------------------------------
        return true;

    }

    /**
     * @return parents of the current position
     */
    public ArrayList<MenuItemPosition> getParents(){
        ArrayList<MenuItemPosition> tmp = new ArrayList<MenuItemPosition>();
        int i, cnt = this.positionSplitted.length-1;
        StringBuffer sb = new StringBuffer("");
        String tok="";
        for(i=0;i<cnt;i++){
            sb.append(tok);
            sb.append(this.positionSplitted[i].label);
            tmp.add(new MenuItemPosition(sb.toString()));
            tok=MenuItemPosition.token;
        }
        return tmp;
    }

    /**
     * @return last word in the position
     */
    public String getLastLabel() {
        return positionSplitted[positionSplitted.length - 1].label;
    }

    /**
     * @return last weight in the position
     */
    public int getLastWeight() {
        return positionSplitted[positionSplitted.length - 1].weight;
    }

    /**
     * Position of the menu item as String sholuld be compared
     * if ordering is needed
     */
    @Override
    public int compareTo(MenuItemPosition o) {
        //System.out.println(this.position+" "+o.position+" >> "+this.position.compareTo(o.position));
        return this.position.compareTo(o.position);
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o){
        //System.out.println(this.uid+"=="+o.toString()+" => "+this.uid.equals(o.toString()));
        return this.uid.equals(o.toString());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.uid != null ? this.uid.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString(){
        return this.uid;
    }
}

