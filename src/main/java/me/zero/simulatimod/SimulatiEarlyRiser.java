package me.zero.simulatimod;

import com.chocohead.mm.api.ClassTinkerers;

public class SimulatiEarlyRiser implements Runnable {
    @Override
    public void run() {
        try {
            ClassTinkerers.enumBuilder("Lnet/minecraft/client/realms/dto/RealmsServer$WorldType").addEnum("CUSTOMIZED");
        } catch (Exception ex) {
            ClassTinkerers.enumBuilder("net/minecraft/class_4877$class_4321").addEnum("CUSTOMIZED");
        }
    }
}
