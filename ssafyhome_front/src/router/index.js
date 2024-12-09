import { createRouter, createWebHistory } from "vue-router";
import { useUserStore } from "@/stores/userStore";
import { storeToRefs } from "pinia";

// 컴포넌트
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
import MainNews from "@/components/news/MainNews.vue";
import KakaoLoginRedirect from "@/components/users/KakaoLoginRedirect.vue";

// 로그인 상태일때
const isLoginUser = async (to, from, next) => {
  const userStore = useUserStore();
  const { isLogin } = storeToRefs(userStore);

  if (isLogin.value) {
    next({ name: "home" });
  } else {
    next();
  }
};

// 비로그인 상태일때
const isNotLoginUser = async (to, from, next) => {
  const userStore = useUserStore();
  const { isLogin } = storeToRefs(userStore);

  if (!isLogin.value) {
    next({ name: "home" });
  } else {
    next();
  }
};

// 로그인 상태일때
const isAdmin = async (to, from, next) => {
  const userStore = useUserStore();
  const { userInfo } = storeToRefs(userStore);
  console.log(userInfo.value.role);
  if (userInfo.value.role != "ADMIN") {
    next({ name: "home" });
  } else {
    next();
  }
};

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
  { path: "/login", name: "login", component: Login, beforeEnter: isLoginUser },
  {
    path: "/regist",
    name: "user-regist",
    component: UserRegister,
    beforeEnter: isLoginUser,
  },
  {
    path: "/userinfo",
    name: "user-info",
    component: MyInfo,
    beforeEnter: isNotLoginUser,
  },
  { path: "/news", name: "news", component: MainNews },
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
        beforeEnter: isAdmin,
      },
      {
        path: "view/edit/:page/:boardNo",
        name: "board-edit",
        component: BoardEdit,
        beforeEnter: isAdmin,
      },
    ],
  },
  {
    path: "/oauth/callback/kakao",
    name: "kakaoRedirect",
    component: KakaoLoginRedirect,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
