package scc210game.engine.databasesystem;

public class DatabaseOperation {


    public static final String[] filenames = {"data", "logs"};
    public static final int UPDATE_OPERATION = 0;
    public static final int DELETE_OPERATION = 1;
    public static final int REPLACE_OPERATION = 2;
    public static final int APPEND_OPERATION = 3;
    public static final int GET_KEY_POSITION = 4;
    public static final int GET_KEY_REF_POSITION = 5;
    public static final String DELIMITER = "-";
    public static final String EMPTY_KEY = "null";


    public static final String ADD_KEY_OPERATION = "ADDING KEY";
    public static final String DELETE_KEY_OPERATION = "DELETING KEY";
    public static final String UPDATE_KEY_OPERATION = "UPDATING KEY";
    public static final String CLEAR_KEY_OPERATION = "CLEARING KEY";
    public static final String ADD_KEY_ERROR = "ADDING ERROR";
    public static final String DELETE_KEY_ERROR = "DELETING ERROR";
    public static final String UPDATE_KEY_ERROR = "UPDATING ERROR";
    public static final String CLEAR_KEY_ERROR = "CLEARING ERROR";

    public static final String BUILDING_DATABASE = "BUILDING DATABASE";
    public static final String OPENING_DATABASE = "OPENING_DATABASE";

    public static final String PROVIDING_KEY_OPERATION = "PROVIDING KEY";
    public static final String PROVIDING_KEY_ERROR = "PROVIDING KEY ERROR";



}
