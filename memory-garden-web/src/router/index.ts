import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/RegisterView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/components/MainLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'Garden',
        component: () => import('@/views/GardenView.vue')
      },
      {
        path: 'card/create',
        name: 'CardCreate',
        component: () => import('@/views/CardCreateView.vue')
      },
      {
        path: 'card/:id/edit',
        name: 'CardEdit',
        component: () => import('@/views/CardEditView.vue')
      },
      {
        path: 'card/list',
        name: 'CardList',
        component: () => import('@/views/CardListView.vue')
      },
      {
        path: 'category',
        name: 'CategoryManage',
        component: () => import('@/views/CategoryManageView.vue')
      },
      {
        path: 'review',
        name: 'Review',
        component: () => import('@/views/ReviewView.vue')
      },
      {
        path: 'review/summary',
        name: 'ReviewSummary',
        component: () => import('@/views/ReviewSummaryView.vue')
      },
      {
        path: 'badge',
        name: 'Badge',
        component: () => import('@/views/BadgeView.vue')
      },
      {
        path: 'stats',
        name: 'Stats',
        component: () => import('@/views/StatsView.vue')
      },
      {
        path: 'study-pack',
        name: 'StudyPack',
        component: () => import('@/views/StudyPackView.vue')
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/ProfileView.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/**
 * 路由守卫：未登录重定向到登录页
 */
router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth !== false && !token) {
    next({ name: 'Login' })
  } else if ((to.name === 'Login' || to.name === 'Register') && token) {
    next({ name: 'Garden' })
  } else {
    next()
  }
})

export default router
