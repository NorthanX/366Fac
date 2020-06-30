package com.Service;

import com.Dao.AccessoryDao;
import com.Model.Accessory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AccessoryService {
    @Autowired
    private AccessoryDao accessoryDao;

    public List<Accessory> getAccessory(int ID){
        return accessoryDao.getAccessoryByID(ID);
    }

    public void addAccessory(Accessory accessory){
        accessoryDao.insertAccessory(accessory);
    }

    public void deleteAccessory(String name, int ID){
        accessoryDao.deleteAccessory(name,ID);
    }

}
