package org.nyusziful.pictureorganizer.Model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.nyusziful.pictureorganizer.DB.AbstractDAO;
import java.util.List;

public class DriveDAOImplHib extends CRUDDAOImpHib<DriveDTO> implements DriveDAO {
}
