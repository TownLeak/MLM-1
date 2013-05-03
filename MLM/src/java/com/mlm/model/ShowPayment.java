/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mlm.model;

import com.mlm.action.Action;
import com.mlm.bean.Order;
import com.mlm.bean.Payment;
import com.mlm.dbutility.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HR
 */
public class ShowPayment implements Action {
   
    DBConnection db;
    public ShowPayment() {
        db = new DBConnection();
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        ArrayList<Payment> AllPayment= null;   
        ArrayList<Order> all_order = null;
        //Payment temp_obj_payment = null;
        
        try{
            //Order_ID resultset
            all_order = new ArrayList<Order>();
            int mem_id = Integer.parseInt(req.getAttribute("cur_user").toString());
            ResultSet rs_order = db.querys("select ORDER_ID FROM TBL_ORDER WHERE (ORDER_ID NOT IN (SELECT ORDER_ID FROM PAYMENT)) AND MEM_ID="+mem_id);
            while(rs_order.next()){
                Order temp_order = new Order();
                temp_order.setOrder_id(rs_order.getInt("ORDER_ID"));
                all_order.add(temp_order);
            }
             //Payment Resultset
            AllPayment = new ArrayList<Payment>();
            ResultSet rs = db.querys("select * from PAYMENT");
            while(rs.next()){
                Payment Pay_Obj = new Payment();
                Pay_Obj.setAmount(rs.getInt("AMOUNT"));
                Pay_Obj.setMem_id(rs.getInt("MEM_ID"));
                Pay_Obj.setOrder_id(rs.getInt("ORDER_ID"));
                Pay_Obj.setPay_date(rs.getString("PAY_DATE"));
                Pay_Obj.setPay_id(rs.getInt("PAY_ID"));
                Pay_Obj.setRemark(rs.getString("REMARK"));
                AllPayment.add(Pay_Obj);
            }
        }catch (SQLException ex) {
            ex.getMessage();
        }
        
        int rs_payID = 0;
        rs_payID = db.queryint("select max(PAY_ID)+1 from PAYMENT");
        Payment Pr = new Payment();
        Pr.setPay_id(rs_payID);
        
        req.setAttribute("AllPayment", AllPayment);
        req.setAttribute("Order", all_order);
        //req.setAttribute("Payment",temp_obj_payment);
        req.setAttribute("payID",Pr);
        return "Payment.jsp";
    }
}