package main.java.services;

import main.java.ClassTypes.OfflineMessage;
import main.java.Main;
import net.dv8tion.jda.core.entities.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.carrotsearch.sizeof.RamUsageEstimator;

public class CacheService extends Thread{

    public Map<String, Object> cacheTree;

    public CacheService(){
        cacheTree = new HashMap<String, Object>();
        setupCaches();
    }

    public void setupCaches(){
        //CacheTree Children
        cacheTree.put("message-cache", new HashMap<String, OfflineMessage>());
    }

    @Override
    public void run(){

        this.setName("CacheService");
        Main.getResources().services.add(this);

        cacheTree = new HashMap<String, Object>();

        setupCaches();

        while(Main.getResources().killInitiated == 0) {
            if (RamUsageEstimator.sizeOf(cacheTree) > Long.parseLong(Main.getResources().botAdministratorConfig.get("cache-byte_limit").toString())) {
                cacheTree.clear();
                setupCaches();
                Main.getResources().coreService.SendDebugToHome("Cleared Cache", "Cleared Cache due to it reaching it's limit.", "");

            }
        }

        Main.getResources().services.remove(this);
        this.interrupt();

    }


}
