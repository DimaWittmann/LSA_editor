package internal_representation;

/**
 *
 * @author Wittman
 */
public enum TrigerState{ 
    ONE, ZERO, DOES_NOT_MATTER;

    @Override
    public String toString() {
        switch (this){
            case DOES_NOT_MATTER:
                return "*";
            case ONE:
                return "1";
            case ZERO:
                return "0";
            default:
                return "";
        }
    }

};