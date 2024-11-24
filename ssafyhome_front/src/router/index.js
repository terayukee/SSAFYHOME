import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";
import Category from "../views/Category.vue";
import Favorites from "@/components/common/Favorites.vue";
import Login from "@/components/users/Login.vue";
import AptMapView from "@/views/AptMapView.vue";
import BoardView from "@/views/BoardView.vue";
import BoardWirte from "@/components/boards/BoardWirte.vue";
import UserRegister from "@/components/users/UserRegister.vue";
import BoardEdit from "@/components/boards/BoardEdit.vue";
import MyInfo from "@/components/users/MyInfo.vue";



const routes = [
  { path: "/", name: "home", component: Home },
  { path: "/category/:type", name: "category", component: Category },
  {
    path: "/map",
    name: "map",
    component: AptMapView,
    props: (route) => ({
      latitude: parseFloat(route.query.latitude) || 37.5,
      longitude: parseFloat(route.query.longitude) || 127.085,
      maplevel: parseFloat(route.query.maplevel) || 4,
      houseType: route.query.housetype || "apartment",
    }), // 쿼리 파라미터를 props로 전달
  },
  { path: "/favorites", name: "favorite", component: Favorites },
  { path: "/login", name: "login", component: Login },
  { path: "/regist", name: "user-regist", component: UserRegister },
  { path: "/userinfo", name: "user-info", component: MyInfo }
  ,
  {
    path: "/board",
    name: "board",
    component: BoardView,
    redirect: { name: "board-list" },
    children: [
      {
        path: "list",
        name: "board-list",
        component: () => import("@/components/boards/BoardList.vue"),
      },
      {
        path: "list/:page",
        name: "board-list-page",
        component: () => import("@/components/boards/BoardList.vue"),
      },
      {
        path: "view/:page/:boardNo",
        name: "board-view",
        component: () => import("@/components/boards/BoardDetail.vue"),
      },
      {
        path: "view/wirte/:page",
        name: "board-write",
        component: BoardWirte,
      },
      {
        path: "view/edit/:page/:boardNo",
        name: "board-edit",
        component: BoardEdit,  
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
