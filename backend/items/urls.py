from rest_framework import routers
from django.urls import path, include
from .views import ItemViewSet, ItemAndUsagesViewSet

router = routers.DefaultRouter()
router.register(r'items', ItemViewSet)
router.register(r'items_and_usages', ItemAndUsagesViewSet)

urlpatterns = [
    path('', include(router.urls)),
]