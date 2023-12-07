from rest_framework import routers
from django.urls import path, include
from .views import ItemViewSet, ItemAndUsagesViewSet
from .models import Item

router = routers.DefaultRouter()
router.register(r'items', ItemViewSet)
router.register(r'items/(?P<id>\d+)/usages', ItemAndUsagesViewSet, basename='item-usages')

urlpatterns = [
    path('', include(router.urls)),
]