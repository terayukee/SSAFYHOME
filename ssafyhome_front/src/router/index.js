import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";
import Category from "../views/Category.vue";
import Favorites from "@/components/common/Favorites.vue";
import Login from "@/components/users/Login.vue";
import Signup from "@/components/Signup.vue";
import AptMapView from "@/views/AptMapView.vue";
import BoardView from "@/views/BoardView.vue";
import BoardWirte from "@/components/boards/BoardWirte.vue";
import UserRegister from "@/components/users/UserRegister.vue";



const routes = [
  { path: "/", name: "home", component: Home },
  { path: "/category/:type", name: "category", component: Category },
  {
    path: "/map",
    name: "map",
    component: AptMapView,
    props: (route) => ({
      latitude: parseFloat(route.query.latitude) || 37.514575,
      longitude: parseFloat(route.query.longitude) || 127.0495556,
      maplevel: parseFloat(route.query.maplevel) || 5,
    }), // 쿼리 파라미터를 props로 전달
  },
  { path: "/favorites", name: "favorite", component: Favorites },
  { path: "/login", name: "login", component: Login },
  { path: "/regist", name: "user-regist", component: UserRegister },
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
        path: "view/:page/:articleno",
        name: "article-view",
        component: () => import("@/components/boards/BoardDetail.vue"),
      },
      {
        path: "view/wirte/:page",
        name: "board-write",
        component: BoardWirte,
      },
      // {
      //   path: "view/edit/:articleno",  // 수정 페이지 경로 추가
      //   name: "board-edit",
      //   component: BoardUpdate,
      // },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
