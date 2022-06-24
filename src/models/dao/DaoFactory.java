package models.dao;

import models.dao.implementation.DepartmentDaoJDBC;
import models.dao.implementation.SellerDaoJDBC;

public class DaoFactory {

    private DaoFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC();
    }

    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC();
    }

}