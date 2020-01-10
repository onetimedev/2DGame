package scc210game.databasesystem;

public class ProcessManager implements Runnable{

    private DataProcessor dataProcessor;
    private String result;
    private int opcode = 0;

    public ProcessManager(DataProcessor dataProcessor, int opcode)
    {
        this.dataProcessor = dataProcessor;
        this.opcode = opcode;
    }

    @Override
    public void run() {
        switch (opcode)
        {
            case Constants.GET_KEY_POSITION:
                result = dataProcessor.getKeyPosition();
                break;
            case Constants.GET_KEY_REF_POSITION:
                result = dataProcessor.getKeyRefPosition();
        }
    }

    public String getResult()
    {
        return result;
    }
}
