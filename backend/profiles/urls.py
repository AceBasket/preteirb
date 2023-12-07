from rest_framework import routers
from django.urls import path, include
from .views import ProfileViewSet, ProfileAndItemsOwnedViewSet, UsageAndItemWithOwnerViewSet

router = routers.DefaultRouter()
router.register(r'profiles', ProfileViewSet)
router.register(r'profiles/(?P<id>\d+)/items_owned', ProfileAndItemsOwnedViewSet, basename='profile-items')
router.register(r'profiles/(?P<id>\d+)/usages', UsageAndItemWithOwnerViewSet, basename='profile-usages')

urlpatterns = [
    path('', include(router.urls)),
]