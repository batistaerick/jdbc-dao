package app;

import java.util.Date;

import models.dao.DaoFactory;
import models.dao.SellerDao;
import models.entities.Department;
import models.entities.Seller;

public class App {
    public static void main(String[] args) {

        SellerDao sDao = DaoFactory.createSellerDao();

        System.out.println("---------- TEST 1: Seller Find By Id ----------");

        System.out.println(sDao.findById(2));

        System.out.println("\n---------- TEST 2: Seller Find By Department ----------");

        System.out.println(sDao.findByDepartmet(new Department(2, null)));

        System.out.println("\n---------- TEST 3: Seller Find All ----------");

        System.out.println(sDao.findAll());

        System.out.println("\n---------- TEST 4: Seller Insert ----------");

        sDao.insert(new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, new Department(2, null)));

        System.out.println("\n---------- TEST 5: Seller Update ----------");

        sDao.update(new Seller(1, "Erick", "erick@gmail.com", new Date(), 5000.0, new Department(2, null)));

        System.out.println("\n---------- TEST 6: Seller Delete ----------");

        sDao.deleteById(20);

    }
}