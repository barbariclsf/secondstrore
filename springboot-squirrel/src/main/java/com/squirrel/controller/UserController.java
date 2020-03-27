package com.squirrel.controller;

import com.squirrel.common.GgeeConst;
import com.squirrel.dto.AjaxResult;
import com.squirrel.exception.GgeeWebError;
import com.squirrel.pojo.*;
import com.squirrel.service.*;
import com.squirrel.util.DateUtil;
import com.squirrel.util.MD5;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private static Log LOG = LogFactory.getLog(UserController.class);

    @Resource
    private UserService userService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private ImageService imageService;
    @Resource
    private OrderService orderService;
    @Resource
    private GoodsCarService goodsCarService;

    /**
     * 用户注册
     * @param user1
     * @return
     */
    @RequestMapping(value = "/addUser")
    public String addUser(HttpServletRequest request, @ModelAttribute("user") User user1) {
        String url=request.getHeader("Referer");
        User user=userService.getUserByPhone(user1.getPhone());
        if(user==null) {//检测该用户是否已经注册
            String t = DateUtil.getNowTime();
            //对密码进行MD5加密
            String str = MD5.md5(user1.getPassword());
            user1.setCreateAt(t);//创建开始时间
            user1.setPassword(str);
            user1.setGoodsNum(0);
            user1.setStatus((byte)0);
            user1.setPower((byte)10);
            userService.addUser(user1);
        }
        return "redirect:"+url;
    }

    /**
     * 验证登录
     * @param request
     * @param user
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/login")
    public ModelAndView loginValidate(HttpServletRequest request, HttpServletResponse response, HttpSession session,User user, ModelMap modelMap) {
        User cur_user = userService.getUserByPhone(user.getPhone());

        String url=request.getHeader("Referer");
        System.out.println(url);
        if(cur_user != null && cur_user.getStatus() == 0) {
            String pwd = MD5.md5(user.getPassword());
            if(pwd.equals(cur_user.getPassword())) {
                //设置单位为秒，设置为-1永不过期
                request.getSession().setMaxInactiveInterval(24*60*60);    //24小时
                request.getSession().setAttribute("cur_user",cur_user);
                request.getSession().setAttribute("user_id",cur_user.getId());
                return new ModelAndView("redirect:"+url);

            }
        }
        return new ModelAndView("redirect:"+url);
    }

    /**
     * API:验证登录
     */
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult login(HttpServletRequest request, HttpServletResponse response) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            User cur_user = userService.getUserByPhone(phone);
            if(cur_user != null) {
                String pwd = MD5.md5(password);
                if(pwd.equals(cur_user.getPassword())) {
                    //设置单位为秒，设置为-1永不过期
                    request.getSession().setMaxInactiveInterval(24*60*60);    //24小时
                    request.getSession().setAttribute(GgeeConst.CUR_USER,cur_user);
                    ajaxResult.setData(cur_user);
                } else {
                    return AjaxResult.fixedError(GgeeWebError.WRONG_PASSWORD);
                }
            } else {
                return AjaxResult.fixedError(GgeeWebError.WRONG_USERNAME);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return AjaxResult.fixedError(GgeeWebError.COMMON);
        }
        return ajaxResult;
    }

    /**
     * API:添加用户
     */
    @RequestMapping(value="/api/addUser", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult addUser(@RequestBody User user) {
        AjaxResult ajaxResult = new AjaxResult();
        User existUser = userService.getUserByPhone(user.getPhone());
        if(existUser == null) {//检测该用户是否已经注册
            String t = DateUtil.getNowTime();
            //对密码进行MD5加密
            String str = MD5.md5(user.getPassword());
            user.setCreateAt(t);//创建开始时间
            user.setPassword(str);
            user.setGoodsNum(0);
            user.setStatus((byte)0);
            userService.addUser(user);
            return new AjaxResult().setData(1);
        }
        return AjaxResult.fixedError(GgeeWebError.AREADY_EXIST_PHONE);
    }

    /**
     * API:更新用户
     */
    @RequestMapping(value="/api/updateUser", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateUser(@RequestBody User user) {
        AjaxResult ajaxResult = new AjaxResult();
        //对密码进行MD5加密
        String str = MD5.md5(user.getPassword());
        user.setPassword(str);
        boolean result = userService.updateUserById(user);
        return new AjaxResult().setData(result);
    }

    /**
     * API:删除用户
     */
    @DeleteMapping("/api/deleteUser/{id}")
    @ResponseBody
    public AjaxResult deleteUser(@PathVariable int id) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean result = userService.deleteUserById(id);
        return new AjaxResult().setData(result);
    }

    /**
     * API:冻结用户
     */
    @RequestMapping(value="/api/freezeUser/{id}", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult freezeUser(@PathVariable int id) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean result = userService.freezeUser(id);
        return new AjaxResult().setData(result);
    }

    /**
     * API:解冻用户
     */
    @RequestMapping(value="/api/unfreezeUser/{id}", method= RequestMethod.POST)
    @ResponseBody
    public AjaxResult unfreezeUser(@PathVariable int id) {
        AjaxResult ajaxResult = new AjaxResult();
        boolean result = userService.unfreezeUser(id);
        return new AjaxResult().setData(result);
    }

    /**
     * 更改用户名
     * @param request
     * @param user
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/changeName")
    public ModelAndView changeName(HttpServletRequest request,User user,ModelMap modelMap) {
        String url=request.getHeader("Referer");
        //从session中获取出当前用户
        User cur_user = (User)request.getSession().getAttribute("cur_user");
        cur_user.setUsername(user.getUsername());//更改当前用户的用户名
        userService.updateUserName(cur_user);//执行修改操作
        request.getSession().setAttribute("cur_user",cur_user);//修改session值
        return new ModelAndView("redirect:"+url);
    }

    /**
     * 完善或修改信息
     * @param request
     * @param user
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/updateInfo")
    public ModelAndView updateInfo(HttpServletRequest request,User user,ModelMap modelMap) {
        //从session中获取出当前用户
        User cur_user = (User)request.getSession().getAttribute("cur_user");
        cur_user.setUsername(user.getUsername());
        cur_user.setQq(user.getQq());
        userService.updateUserName(cur_user);//执行修改操作
        request.getSession().setAttribute("cur_user",cur_user);//修改session值
        return new ModelAndView("redirect:/user/basic");
    }
    /**
     * 用户退出
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().setAttribute("cur_user",null);
        return "redirect:/goods/homeGoods";
    }


    /**
     * 个人中心
     * @return
     */
    @RequestMapping(value = "/home")
    public String home(HttpServletRequest request, Model model) {
        User cur_user = (User) request.getSession().getAttribute("cur_user");
        model.addAttribute("cur_user", cur_user);
        return "/user/home";
    }

    /**
     * 个人信息设置
     * @return
     */
    @RequestMapping(value = "/basic")
    public String basic(HttpServletRequest request, Model model) {
        User cur_user = (User) request.getSession().getAttribute("cur_user");
        model.addAttribute("cur_user", cur_user);
        return "/user/basic";
    }

    /**
     * 我的闲置
     * 查询出所有的用户商品以及商品对应的图片
     * @return  返回的model为 goodsAndImage对象,该对象中包含goods 和 images，参考相应的类
     */
    @RequestMapping(value = "/allGoods")
    public ModelAndView goods(HttpServletRequest request) {
        User cur_user = (User)request.getSession().getAttribute("cur_user");
        Integer userId = cur_user.getId();
        List<Goods> goodsList = goodsService.getGoodsByUserId(userId);
        List<GoodsExtend> goodsAndImage = new ArrayList<GoodsExtend>();
        int j = 0;
        for (int i = 0; i < goodsList.size() ; i++) {
            //将用户信息和image信息封装到GoodsExtend类中，传给前台
            GoodsExtend goodsExtend = new GoodsExtend();
            Goods goods = goodsList.get(i);
            if(goods.getState() == 0){
                List<Image> images = imageService.getImagesByGoodsPrimaryKey(goods.getId());
                goodsExtend.setGoods(goods);
                goodsExtend.setImages(images);
                goodsAndImage.add(j, goodsExtend);
                j++;
            }
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("cur_user", cur_user);
        mv.addObject("goodsAndImage",goodsAndImage);
        mv.setViewName("/user/goods");
        return mv;
    }
    /*加入购物车
    * */
    @RequestMapping(value="/addgoodscar", method= RequestMethod.GET)
    @ResponseBody
    public AjaxResult goodsCar(HttpServletRequest request, @RequestParam(value = "id")Integer id) {
        Goods goods = goodsService.getGoodsByPrimaryKey(id);
        GoodsCar goodsCar = new GoodsCar();
        goodsCar.setCatelogId(id);
        User cur_user = (User)request.getSession().getAttribute("cur_user");
        goodsCar.setUserId(cur_user.getId());
        goodsCar.setName(goods.getName());
        goodsCar.setPrice(goods.getPrice());
        goodsCar.setRealPrice(goods.getRealPrice());
        goodsCar.setStartTime(goods.getStartTime());
        goodsCar.setEndTime(goods.getEndTime());
        goodsCar.setPolishTime(goods.getPolishTime());
        goodsCar.setDescrible(goods.getDescrible());
        goodsCar.setCommetNum(goods.getCommetNum());
        goodsCar.setState(goods.getState());
        Integer result = goodsCarService.insertGoodsCar(goodsCar);
        boolean flag = result == 1 ? true : false;
        return new AjaxResult().setSuccess(flag);
    }
    /*
    * 查询购物车的所有无品*/
    @RequestMapping(value = "/selectcargoods")
    public ModelAndView selectcargoods(HttpServletRequest request){
        User cur_user = (User)request.getSession().getAttribute("cur_user");
        Integer uid = cur_user.getId();
        //根据用户id查询购物车物品
        List<GoodsCar> goodsCars = goodsCarService.selectCarGoods(uid);
        List<GoodsCarExtend> goodsCarAndImage = new ArrayList<GoodsCarExtend>();
        int j = 0;
        for(int i = 0;i < goodsCars.size();i++)
        {
            GoodsCarExtend goodsCarExtend = new GoodsCarExtend();
            GoodsCar goodsCar = goodsCars.get(i);
            Integer pid = goodsCar.getCatelogId();
            Goods goods = goodsService.getGoodsByPrimaryKey(pid);
            if(goods.getState() == 0){
                List<Image> images = imageService.getImagesByGoodsPrimaryKey(goodsCar.getCatelogId());
                goodsCarExtend.setGoodsCar(goodsCar);
                goodsCarExtend.setImages(images);
                goodsCarAndImage.add(j,goodsCarExtend);
                j++;
            }

        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("cur_user", cur_user);
        mv.addObject("goodsCarAndImage",goodsCarAndImage);
        mv.setViewName("/user/goodscar");
        return mv;
    }
    /*
    * 删除购物车某项*/
    @RequestMapping(value="/deletecargoods", method= RequestMethod.GET)
    public ModelAndView deletecargoods(HttpServletRequest request, @RequestParam(value = "id")Integer id) {
        //根据id删除
        goodsCarService.deleteCarGoods(id);
        User cur_user = (User)request.getSession().getAttribute("cur_user");
        Integer uid = cur_user.getId();
        //根据用户id查询购物车物品
        List<GoodsCar> goodsCars = goodsCarService.selectCarGoods(uid);
        List<GoodsCarExtend> goodsCarAndImage = new ArrayList<GoodsCarExtend>();
        for(int i = 0;i < goodsCars.size();i++)
        {
            GoodsCarExtend goodsCarExtend = new GoodsCarExtend();
            GoodsCar goodsCar = goodsCars.get(i);
            List<Image> images = imageService.getImagesByGoodsPrimaryKey(goodsCar.getCatelogId());
            goodsCarExtend.setGoodsCar(goodsCar);
            goodsCarExtend.setImages(images);
            goodsCarAndImage.add(i,goodsCarExtend);
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("cur_user", cur_user);
        mv.addObject("goodsCarAndImage",goodsCarAndImage);
        mv.setViewName("/user/goodscar");
        return mv;
}
    /*
    * 我已购买的*/
    @RequestMapping(value = "/mybuyed")
    public ModelAndView buy(HttpServletRequest request){
        User cur_user = (User)request.getSession().getAttribute("cur_user");
        Integer uid = cur_user.getId();

        List<Order> orderslist = orderService.seletOrderById(uid);
        List<OrderExtend> orderAndImage = new ArrayList<OrderExtend>();
        for (int i = 0; i < orderslist.size() ; i++) {
            //将用户信息和image信息封装到GoodsExtend类中，传给前台
            OrderExtend orderExtend = new OrderExtend();
            Order order = orderslist.get(i);
            Date date = order.getTime();
            SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString  = dateformat.format(date);
            List<Image> images = imageService.getImagesByGoodsPrimaryKey(order.getPid());
            Goods good = goodsService.getGoodsByPrimaryKey(order.getPid());
            //得到商家id
            Integer userid = good.getUserId();
            //得到商家信息
            User user = userService.selectByPrimaryKey(userid);
            //获取商家名字
            String uname = user.getUsername();
            //获取商品名称
            String name = good.getName();
            orderExtend.setUnmae(uname);
            orderExtend.setOrder(order);
            orderExtend.setName(name);
            orderExtend.setTime(dateString);
            orderExtend.setImages(images);

            orderAndImage.add(i, orderExtend);
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("cur_user", cur_user);
        mv.addObject("orderAndImage",orderAndImage);
        mv.setViewName("/user/buyed");
        return mv;
    }
    /*我卖出的*/
    @RequestMapping("/selledgoods")
    public ModelAndView mySelledGoods(HttpServletRequest request){
        //登录用户id
        User cur_user = (User)request.getSession().getAttribute("cur_user");
        Integer uid = cur_user.getId();
        //联合查询 订单表和物品表
        List<Goods> goodsList = goodsService.seletOrderAndGoods();
        List<GoodsAndUserOrder> goodsAndImage =  new ArrayList<GoodsAndUserOrder>();
        int  j = 0;
        for(int i = 0;i < goodsList.size();i++)
        {
            //获取商家的userid
            Goods goods = goodsList.get(i);
            Integer userId = goods.getUserId();
            //两者相等说明是自己卖出的
            if(userId == uid){
                User seller = userService.getUserById(userId);
                Order order = orderService.seletOneOrderByPid(goods.getId());
                Date date = order.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(date);
                String orderNum = order.getOrderNum();
                String tradeNum = order.getTradenum();
                List<Image> images = imageService.getImagesByGoodsPrimaryKey(goods.getId());
                GoodsAndUserOrder goodsAndUserOrder = new GoodsAndUserOrder();
                goodsAndUserOrder.setGoods(goods);
                goodsAndUserOrder.setDate(dateString);
                goodsAndUserOrder.setOrderNum(orderNum);
                goodsAndUserOrder.setTradeNum(tradeNum);
                goodsAndUserOrder.setImages(images);
                goodsAndUserOrder.setUser(seller);
                goodsAndImage.add(j,goodsAndUserOrder);
                j++;
            }
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("cur_user", cur_user);
        mv.addObject("goodsAndImage",goodsAndImage);
        mv.setViewName("/user/selledgoods");
        return mv;

    }
}
