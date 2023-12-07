from rest_framework import routers
from django.urls import path, include
from .views import ProfileViewSet, ProfileAndItemsOwnedViewSet

router = routers.DefaultRouter()
router.register(r'profiles', ProfileViewSet)
router.register(r'profiles_and_items_owned', ProfileAndItemsOwnedViewSet)

urlpatterns = [
    path('', include(router.urls)),
]