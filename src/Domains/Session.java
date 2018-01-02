package Domains;

import Interfaces.IChatServerManager;
import Interfaces.IUser;

public class Session {
    private IChatServerManager server;
    private IUser user;

    public Session(IChatServerManager server) {
        this.server = server;
    }

    public IChatServerManager getServer() {
        return server;
    }

    public IUser getUser() {
        return user;
    }

    public void setUser(IUser user) {
        this.user = user;
    }
}
