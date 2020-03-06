package scc210game.engine.databasesystem;

class DatabaseOperation {


    static final String[] filenames = {"data", "logs"};
    static final int UPDATE_OPERATION = 0;
    static final int DELETE_OPERATION = 1;
    static final int REPLACE_OPERATION = 2;
    static final int APPEND_OPERATION = 3;
    static final int GET_KEY_POSITION = 4;
    static final int GET_KEY_REF_POSITION = 5;
    static final String DELIMITER = "\0";
    static final String EMPTY_KEY = "null";


    static final String ADD_KEY_OPERATION = "ADDING KEY";
    static final String DELETE_KEY_OPERATION = "DELETING KEY";
    static final String UPDATE_KEY_OPERATION = "UPDATING KEY";
    static final String CLEAR_KEY_OPERATION = "CLEARING KEY";
    static final String ADD_KEY_ERROR = "ADDING ERROR";
    static final String DELETE_KEY_ERROR = "DELETING ERROR";
    static final String UPDATE_KEY_ERROR = "UPDATING ERROR";
    static final String CLEAR_KEY_ERROR = "CLEARING ERROR";

    static final String BUILDING_DATABASE = "BUILDING DATABASE";
    static final String OPENING_DATABASE = "OPENING_DATABASE";

    static final String PROVIDING_KEY_OPERATION = "PROVIDING KEY";
    static final String PROVIDING_KEY_ERROR = "PROVIDING KEY ERROR";

    static final String COLLECT_ALL_KEYS = "COLLECT ALL KEYS";



}
