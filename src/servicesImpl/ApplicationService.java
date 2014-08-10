/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servicesImpl;

import helpers.HibernateUtil;
import org.hibernate.Session;

/**
 *
 * @author Skrzypek
 */
public abstract class ApplicationService {
    protected Session session;

    public ApplicationService() {
        if(session == null) {
            session = HibernateUtil.getSessionFactory().openSession();
        }
    }

    public void closeSession() {
        if (session.isOpen()) {
            session.close();
        }
    }
}
