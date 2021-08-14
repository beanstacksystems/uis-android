package com.bss.uis.database.dao;

import com.bss.uis.context.UISApplicationContext;
import com.bss.uis.database.UISDataBaseClient;
import com.bss.uis.database.UISDatabase;
import com.bss.uis.database.asynctasks.DeleteMasterData;
import com.bss.uis.database.asynctasks.InsertMasterData;
import com.bss.uis.database.asynctasks.RetrieveMasterDataByType;
import com.bss.uis.database.entity.MasterData;

import java.util.List;

import lombok.SneakyThrows;

public class MasterDAORepository {
    private MasterDAO masterDAO;
    public MasterDAORepository(UISApplicationContext context) {
        UISDatabase database = UISDataBaseClient.getInstance(context).getUisDatabase();
        masterDAO = database.masterDAO();
    }
    public void insert(List<MasterData> masterDataList) {
        new InsertMasterData(masterDAO).execute(masterDataList.toArray(new MasterData[0]));
    }
    @SneakyThrows
    public List<MasterData> getMasterByType(String masterType) {
        return new RetrieveMasterDataByType(masterDAO,masterType).execute().get();
    }
    public void deleteMaster()
    {
        new DeleteMasterData(masterDAO).execute();
    }
}
