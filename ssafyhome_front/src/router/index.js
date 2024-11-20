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
  { path: "/map", name: "map", component: AptMapView },
  { path: "/favorites", name: "favorite", component: Favorites },
  { path: "/login", name: "login", component: Login },
  { path: "/regist", name: "user-regist", component: UserRegister },
  { path: "/board", name: "board", component: BoardView,
    redirect: { name: "board-list"},
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
        component: BoardWirte
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
