package com.tojo;

import dao.DishOrderDao;
import dao.OrderDao;
import entity.DishOrder;
import entity.Order;
import entity.OrderStatus;
import entity.Status;

import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DishOrderDao dishOrderdao = new DishOrderDao();
        OrderDao orderDao = new OrderDao();

        Order order = new Order();
        order.setUniqueReference(1);
        order.setStatusOrder(orderDao.getAllOrderStatusInOrder(order.getUniqueReference()));
        order.setListDishOrder(dishOrderdao.getAllDishOrderByReferenceOrder(order.getUniqueReference()));

        System.out.println(order.getActualStatus());
    }
}