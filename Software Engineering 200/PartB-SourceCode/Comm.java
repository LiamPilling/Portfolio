import java.util.*;

public abstract class Comm
{
    public Comm() {}
    public void send(String message) {}
    protected abstract void receive(String message);
}
