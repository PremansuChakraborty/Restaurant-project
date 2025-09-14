package tech.zeta.restaurant.project.dao;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.restaurant.project.models.TableInfo;
import tech.zeta.restaurant.project.models.enums.TableInfoEnum;
import tech.zeta.restaurant.project.util.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class TableInfoDAOImp implements TableInfoDAO{
    private static Connection connection;
    @Override
    public void updateStatus(int tableId, TableInfoEnum status) {
        if (tableId==0 || status==null){
            log.error("TableInfoDAOImp updateStatus table id or status issue....");
        }
        else {
            try {
                connection = DBConnection.getInstance().getConnection();
                String sql = "update tableinfo set status=? WHERE id = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, status.name());
                preparedStatement.setInt(2, tableId);
                int row = preparedStatement.executeUpdate();
                if(row>0){
                    log.info("Table updated successfully....... ");
                }
            } catch (IOException | SQLException e) {
                log.error("TableInfoDAOImp sql query fail to execute: {}", e.getMessage());
            }
        }
    }

    @Override
    public boolean addTable(TableInfo tableInfo) {
        if (tableInfo == null || tableInfo.getStatus()==null ||
                tableInfo.getCapacity()==0){
            throw new IllegalArgumentException("TableInfoDAOImp object have some missing properties");
        }
        else {
            try {
                connection = DBConnection.getInstance().getConnection();
                String sql = "INSERT INTO tableinfo (capacity,status) " +
                        "VALUES (?,?);";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, tableInfo.getCapacity());
                preparedStatement.setString(2,tableInfo.getStatus().name());

                int row = preparedStatement.executeUpdate();
                if (row != 0) {
                    log.info("New table is added");
                    return true;
                }
            } catch (IOException | SQLException e) {
                log.error("TableInfoDAOImp sql query fail to execute: {}", e.getMessage());
            }
            return false;
        }
    }

    @Override
    public List<TableInfo> getActiveTables() {
        List<TableInfo> demoList = new ArrayList<>();
        log.debug("getAvailableTables method start");
        try {
            log.debug("getAvailableTables method main");
            connection = DBConnection.getInstance().getConnection();
            String sql = "select * from tableinfo where status='ACTIVE';";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet != null) {
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    int capacity = resultSet.getInt(2);
                    String status = resultSet.getString(3);
//                    System.out.println(id + " | " + name + " | " + phone + " | " + email + " | " + password);
                    TableInfo demo=new TableInfo(capacity,TableInfoEnum.valueOf(status));
                    demo.setId(id);
                    demoList.add(demo);
                }

            } else {
                throw new RuntimeException("TableInfoDAOImp issue in resultSet");
            }
        } catch (IOException  | SQLException e) {
            log.error("TableInfoDAOImp sql query fail to execute: {}", e.getMessage());
        }
        log.debug("getAvailableTables method successfully end");
        return demoList;

    }

    @Override
    public int bestTableAccordingToCapacity(int capacity) {
        List<TableInfo> list=this.getActiveTables();
        int id=0,cap=Integer.MAX_VALUE;
        for(TableInfo table:list){
            if(table.getCapacity()>=capacity && cap>table.getCapacity()) id=table.getId();
        }
        if(id==0) log.error("No Table found........");
        return id;
    }

    @Override
    public TableInfo getTableById(int tableId) {
        if (tableId==0) return null;
        else {
            try {
                connection = DBConnection.getInstance().getConnection();
                String sql = "SELECT * FROM tableinfo WHERE id = ?;";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, tableId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    int capacity = resultSet.getInt(2);
                    String status = resultSet.getString(3);
//                    System.out.println(id + " | " + name + " | " + phone + " | " + email + " | " + password);
                    TableInfo demo=new TableInfo(capacity,TableInfoEnum.valueOf(status));
                    demo.setId(id);
                    return demo;
                }
            } catch (IOException |SQLException e) {
                log.error("CustomerDAOImp sql query fail to execute: {}", e.getMessage());
            }
            return null;
        }
    }
}
