package com.ccrm.interfaces;

import java.util.List;


public interface Persistable<T> {
   
    void saveToFile(String filePath) throws Exception;

    
    List<T> loadFromFile(String filePath) throws Exception;

    void exportToCSV(String filePath) throws Exception;

    List<T> importFromCSV(String filePath) throws Exception;
}
