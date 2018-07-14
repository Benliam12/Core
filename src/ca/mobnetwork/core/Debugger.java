package ca.mobnetwork.core;

/**
 * Created by Benliam12 on 2018-07-14.
 */
public class Debugger {

    private static Debugger instance = new Debugger();
    private boolean status = false;

    public Debugger()
    {
    }

    public void enable()
    {

    }

    public void disable()
    {
    }

    public void set(boolean status)
    {
        if(status)
        {
            this.status = status;
        }
        this.status = false;
    }

}
