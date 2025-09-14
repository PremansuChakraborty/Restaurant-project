package tech.zeta.restaurant.project.dao;

import tech.zeta.restaurant.project.models.TableInfo;
import tech.zeta.restaurant.project.models.enums.TableInfoEnum;

import java.util.List;

public interface TableInfoDAO {
      void updateStatus(int tableId, TableInfoEnum status);
      boolean addTable(TableInfo tableInfo);
      List<TableInfo> getActiveTables();
      int bestTableAccordingToCapacity(int capacity);
      TableInfo getTableById(int tableId);
}
