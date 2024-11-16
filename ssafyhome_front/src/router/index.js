import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";
import Category from "../views/Category.vue";
import Favorites from "@/components/Favorites.vue";
import Login from "@/components/Login.vue";
import Signup from "@/components/Signup.vue";
import AptMapView from "@/views/AptMapView.vue";

const routes = [
  { path: "/", name: "Home", component: Home },
  { path: "/category/:type", name: "Category", component: Category },
  { path: "/map", name: "Map", component: AptMapView },
  { path: "/favorites", name: "Favorites", component: Favorites },
  { path: "/login", name: "Login", component: Login },
  { path: "/signup", name: "Signup", component: Signup },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
