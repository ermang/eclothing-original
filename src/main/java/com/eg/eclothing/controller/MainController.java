package com.eg.eclothing.controller;

import com.eg.eclothing.dto.*;
import com.eg.eclothing.service.MainService;
import com.eg.eclothing.service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
//@CrossOrigin(value = "http://localhost:4200" )//TODO: FIXME
public class MainController {

    private final MainService mainService;
    private final ShoppingCartService shoppingCartService;
//    private final CreateUserDTOValidator createUserDTOValidator;
//    private final CreateTopicDTOValidator createTopicDTOValidator;
//    private final CreateThreadDTOValidator createThreadDTOValidator;

    public MainController(MainService mainService, ShoppingCartService shoppingCartService) {
        this.mainService = mainService;
        this.shoppingCartService =shoppingCartService;

    }

    @RequestMapping("/")
    public String greeting() {
        return "Welcome to Ecommerce";
    }

    @PostMapping("/login")
    public boolean login() {
        return true;
    }

    @PostMapping("/readBaseProduct")
    public Long createBaseProduct(@RequestBody CreateBaseProduct createBaseProduct) {
        Long result = mainService.createBaseProduct(createBaseProduct);
        return result;
    }

    @PostMapping("/product")
    public Long createProduct(@RequestBody CreateProduct createProduct) {
        Long result = mainService.createProduct(createProduct);
        return result;
    }

    @PostMapping("/stock")
    public Long createStock(@RequestBody CreateStock createStock) {
        Long result = mainService.createStock(createStock);

        return result;
    }

    @GetMapping("/baseProducts")
    public ReadBaseProducts readBaseProductList(){
        ReadBaseProducts result = mainService.readAllBaseProducts();

        return result;
    }

    @GetMapping("/products")
    public ReadProductList readProductList(@RequestParam(required = false)  String category,
                                           @RequestParam(required = false) BigDecimal minPrice,
                                           @RequestParam(required = false)  BigDecimal maxPrice){
        ReadProductList result = mainService.readAllProducts(category, minPrice, maxPrice);
        return result;
    }

    @GetMapping("/product/{productId}/stock")
    public ReadProductStock readProductStock(@PathVariable long productId) {
        ReadProductStock result = mainService.readProductStock(productId);

        return result;
    }

    @PostMapping("/cart")
    public boolean addItemToCart(@RequestBody AddProductToCart addProductToCart) {
        boolean result = shoppingCartService.addItemToCart(addProductToCart);

        return result;
    }

    @GetMapping("/cart")
    public ReadCart readCart() {
        ReadCart result = shoppingCartService.readUserCart();

        return result;
    }

    @PostMapping("/user")
    public Long createUser(@RequestBody CreateUserDTO createUserDTO) {
        Long result = mainService.createUser(createUserDTO);

        return result;
    }
//
//    @PostMapping("/coupon")
//    public Long createCoupon(@RequestBody CreateCouponDTO createCouponDTO) {
//        Long result = mainService.createCoupon(createCouponDTO);
//
//        return result;
//    }

//    @PostMapping("/comment")
//    public Long createComment(@RequestBody CreateCommentDTO createCommentDTO) {
//        createThreadDTOValidator.validate(createCommentDTO);
//        Long result = mainService.createComment(createCommentDTO);
//
//        return result;
//    }
//
//    @PostMapping("/topic/like/{topicId}")
//    public Long likeTopic(@PathVariable Long topicId) {
//        Long result = mainService.likeTopic(topicId);
//
//        return result;
//    }
//
//    @PostMapping("/comment/like/{commentId}")
//    public Long likeComment(@PathVariable Long commentId) {
//        Long result = mainService.likeComment(commentId);
//
//        return result;
//    }
//
//    @GetMapping("/topic/{topicName}")
//    public TopicDTO readTopic(@PathVariable String topicName) {
//        TopicDTO topicDTO = mainService.readTopic(topicName);
//
//        return topicDTO;
//    }
//
////    @GetMapping("/topic/{topicName}")
////    public CommentPageDTO readCommentsFromTopic(@PathVariable String topicName, @RequestParam("page") int page) {
////        CommentPageDTO threadDTOs = mainService.readCommentsFromTopic(topicName,  PageRequest.of(page, 2));
////
////        return threadDTOs;
////    }
//
//    @GetMapping("/thread/{username}")
//    public CommentPageDTO readThreadsFromUser(@PathVariable String username, @RequestParam("page") int page) {
//        CommentPageDTO commentPageDTO = mainService.readThreadsFromUser(username, PageRequest.of(page, 2));
//
//        return commentPageDTO;
//    }
//
//    @GetMapping("/comments/topic/{topicId}")
//    public CommentPageDTO readCommentsFromTopic(@PathVariable Long topicId, @RequestParam("page") int page) {
//        CommentPageDTO commentPageDTO = mainService.readCommentsFromTopic(topicId, PageRequest.of(page, 2));
//
//        return commentPageDTO;
//    }
//
//    @GetMapping("/topics")
//    public List<TopicDTO> readTopics() {
//        List<TopicDTO> topicDTOs = mainService.readTopics();
//
//        return topicDTOs;
//    }
//
////    @GetMapping("/topics/recent")
////    public List<TopicDTO> readMostRecentlyUpdatedTopics() {
////        List<TopicDTO> topicDTOs = mainService.readMostRecentlyUpdatedTopics();
////
////        return topicDTOs;
////    }
//
//    @GetMapping("/topics/recent")
//    public TopicPageDTO readRecentTopics(@RequestParam("page") int page) {
//        TopicPageDTO topicPageDTO = mainService.readRecentTopics(PageRequest.of(page, 25));
//
//        return topicPageDTO;
//    }
//
////    @GetMapping("/threads/recent")
////    public CommentPageDTO readRecentThreads(@RequestParam("page") int page) {
////        CommentPageDTO commentPageDTO = mainService.readRecentThreads(PageRequest.of(page, 25));
////
////        return commentPageDTO;
////    }
//
//    @GetMapping("/topics/search")
//    public List<TopicDTO> searchByTopicName(@RequestParam("topicName") String topicName) {
//        List<TopicDTO> topicDTOs = mainService.searchByTopicName(topicName);
//
//        return topicDTOs;
//    }
}
