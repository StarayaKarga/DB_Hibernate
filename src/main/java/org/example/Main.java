package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        /*Course course5 = session.get(Course.class, 1);  //Получаем курс с id = 1
        System.out.println(course5.getName());*/


        /*Course course = new Course();
        course.setName("Новый курс");
        course.setType(CourseType.BUSINESS);
        course.setTeacherId(1);
        session.save(course);           //ДЛЯ СОЗДАНИЯ   */

        /*Course course = session.get(Course.class, 47);
        course.setName("Совсем новый курс");
        session.save(course);          //ДЛЯ ОБНОВЛЕНИЯ УЖЕ СУЩЕСТВУЮЩЕГО*/

        /*Course course = session.get(Course.class, 47);
        session.delete(course);        //УДАЛЯЕТ УЖЕ СУЩЕСТВУЮЩИЙ КУРС*/

        /*Course course = session.get(Course.class, 1);
        System.out.println(course.getTeacher().getName());*/

        Course course = session.get(Course.class, 1);
        List<Student> students = course.getStudents();
        for (Student student : students){
            System.out.println(student.getName());
        }
        System.out.println("\n" +"КУРСЫ:");

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Course> query = builder.createQuery(Course.class);
        Root<Course> root = query.from(Course.class);
        /*query.select(root); //Полностью таблицу - класс*/
        query.select(root).where(builder.greaterThan(root.get("price"), 100000)).orderBy(builder.desc(root.get("price")));
        List<Course> courseList = session.createQuery(query).setMaxResults(5).getResultList();

        for (Course course1 : courseList){
            System.out.println(course1.getName() + " - " + course1.getPrice() /*+ course1.getTeacher().getName()*/);
        }

        System.out.println("\n");
        String hql = "From " + Course.class.getSimpleName() + " Where price > 120000";
        List<Course> courseList1 = session.createQuery(hql).getResultList();
        System.out.println(courseList1.size());
        for (Course course1 : courseList1){
            System.out.println(course1.getName() + " - " + course1.getPrice());
        }

        transaction.commit();
        sessionFactory.close();

    }
}