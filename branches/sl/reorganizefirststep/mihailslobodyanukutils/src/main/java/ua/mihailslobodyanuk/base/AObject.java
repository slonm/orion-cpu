package ua.mihailslobodyanuk.base;

/**
 * Абстрактный класс ...
 * @author sl
 */
public abstract class AObject {

    private final int hashSeed;
    private int hash;
    private final int hashMul;
    private StringBuffer buf;

    /**
     * @param hashSeed -
     * @param hashMul -
     */
    public AObject(int hashSeed, int hashMul) {
        this.hashSeed = hashSeed;
        this.hashMul = hashMul;
    }

    @Override
    public String toString() {
        buf = new StringBuffer();
        aToString();
        return buf.toString();
    }

    /*
     * public void aToString() {
     * <pre>
     *      toStringBegin(Sub.class);
     *      toStringField("f1", f1, false));
     *      toStringField("f2", String.f1, false));
     *      super.toString();
     *      toStringEnd();
     * }
     * </pre>
     */
    /**
     *
     */
    public abstract void aToString();

    /**
     *
     * @param clazz
     */
    public final void aToStringBegin(Class clazz) {
        buf.append(clazz.getName() + ": [");
    }

    /**
     *
     */
    public final void aToStringEnd() {
        buf.append("]");
    }

    /**
     *
     * @param fieldName
     * @param field
     * @param isLast
     */
    public final void aToStringField(String fieldName, Object field, boolean isLast) {
        String q = "";
        if (field instanceof String) {
            q = "\"";
        }
        else {
            if ((field instanceof java.util.Date) ||
                    (field instanceof java.util.Calendar)) {
                q = "#";
            }
            buf.append(fieldName);
            buf.append("=");
            if (field == null) {
                buf.append("<NULL>");
            }
            else {
                buf.append(q);
                buf.append(field);
                buf.append(q);
            }
            buf.append(isLast ? ", " : "");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return aEquals(obj);
    }

    /**
     * Пример переопределения:
     * z - примитивного типа
     * <pre>
     * public boolean aEquals(Sub obj){
     *      return super.aEquals(obj)&&
     *             aEqualsField(f1, obj.f1)&&
     *             aEqualsField(f2, obj.f2)&&
     *             z==other.z;
     * }
     * </pre>
     * @param obj 
     * @return
     */
    public abstract boolean aEquals(Object obj);

    /**
     *
     * @param <O>
     * @param fieldObj1
     * @param fieldObj2
     * @return
     */
    public final <O> boolean aEqualsField(O fieldObj1, O fieldObj2) {
        return (fieldObj1 == null) ? false : fieldObj1.equals(fieldObj2);
    }

    /**
     * Пример переопределения:
     * <pre>
     * public void aHashCode(){
     *      super.aHashCode();
     *      aHashField(f1);
     *      aHashField(f2);
     * }
     * </pre>
     */
    public abstract void aHashCode();

    /**
     *
     * @param field
     */
    public final void aHashField(byte field) {
        hash = hashMul * hash + field;
    }

    /**
     *
     * @param field
     */
    public final void aHashField(char field) {
        hash = hashMul * hash + field;
    }

    /**
     *
     * @param field
     */
    public final void aHashField(short field) {
        hash = hashMul * hash + field;
    }

    /**
     *
     * @param field
     */
    public final void aHashField(int field) {
        hash = hashMul * hash + field;
    }

    /**
     *
     * @param field
     */
    public final void aHashField(long field) {
        hash = hashMul * hash + (int) (field ^ (field >>> 32));
    }

    /**
     *
     * @param field
     */
    public final void aHashField(boolean field) {
        hash = hashMul * hash + (field ? 0 : 1);
    }

    /**
     *
     * @param field
     */
    public final void aHashField(float field) {
        hash = hashMul * hash + Float.floatToIntBits(field);
    }

    /**
     *
     * @param field
     */
    public final void aHashField(double field) {
        aHashField(Double.doubleToLongBits(field));
    }

    /**
     *
     * @param field
     */
    public final void aHashField(Object field) {
        hash = hashMul * hash + (field != null ? field.hashCode() : 0);
    }

    @Override
    public int hashCode() {
        hash = hashSeed;
        aHashCode();
        return hash;
    }
}
