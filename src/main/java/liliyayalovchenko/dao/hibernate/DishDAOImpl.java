package liliyayalovchenko.dao.hibernate;

import liliyayalovchenko.dao.DishDAO;
import liliyayalovchenko.domain.Dish;
import liliyayalovchenko.domain.DishCategory;
import liliyayalovchenko.domain.Ingredient;
import liliyayalovchenko.domain.Menu;
import liliyayalovchenko.web.exeptions.DishNotFoundException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class DishDAOImpl implements DishDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void save(Dish dish) {
        if (dish.getMenu() != null) {
            addDishToMenu(dish.getMenu().getId(), dish);
        }
        sessionFactory.getCurrentSession().save(dish);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    private void addDishToMenu(int menuId, Dish dish) {
        Session session = sessionFactory.getCurrentSession();
        Menu menu = session.load(Menu.class, menuId);
        if (menu == null) {
            throw new RuntimeException("Cant get menu by this id");
        } else {
            menu.addDishToMenu(dish);
            session.update(menu);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void saveDish(int id, String name, String dishCategory, double price,
                         int weight, String photoLink) {
        Session session = sessionFactory.getCurrentSession();
        Dish dish = session.load(Dish.class, id);
        createDish(name, dishCategory, price, weight, photoLink, dish);
        session.update(dish);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void saveDish(String name, String dishCategory, double price,
                         int weight, String photoLink, List<Ingredient> ingredients) {
        Session session = sessionFactory.getCurrentSession();
        Dish dish = new Dish();
        dish.setIngredients(ingredients);
        createDish(name, dishCategory, price, weight, photoLink, dish);
        session.save(dish);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<Dish> getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = currentSession.createQuery("select d from Dish d");
        return query.list();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Dish getDishByName(String dishName) {
        Session session = sessionFactory.getCurrentSession();
        Dish dish = (Dish) session.createQuery("select d from Dish d where d.name = :var")
                .setParameter("var", dishName)
                .uniqueResult();
        if (dish != null) {
            return dish;
        } else {
            throw new RuntimeException("Cant get dish by this dish name! Error!");
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void remove(int id) {
        Session session = sessionFactory.getCurrentSession();
        Dish dish = session.load(Dish.class, id);
        session.delete(dish);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Dish getDishById(int dishId) {
        Session session = sessionFactory.getCurrentSession();
        Dish dish = (Dish) session.createQuery("select d from Dish d where d.id = :var")
                .setParameter("var", dishId).uniqueResult();
        if (dish != null) {
            return dish;
        } else {
            throw new RuntimeException("Cant get dish by this id! Error");
        }
    }

    private void createDish(String name, String dishCategory, double price,
                            int weight, String photoLink, Dish dish) {
        setDishCategory(dishCategory, dish);
        dish.setName(name);
        dish.setPrice(price);
        dish.setWeight(weight);
        dish.setPhotoLink(photoLink);
    }

    private void setDishCategory(String dishCategory, Dish dish) {
        for (DishCategory category : DishCategory.values()) {
            if (dishCategory.equals(category.toString())) {
                dish.setDishCategory(category);
            }
        }
    }
}
