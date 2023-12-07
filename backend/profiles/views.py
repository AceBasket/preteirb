from django.shortcuts import render
from rest_framework import viewsets
from .models import Profile
from .serializers.common import ProfileSerializer
from .serializers.specialized import ProfileAndItemsOwnedSerializer
from usages.serializers.specialized import UsageAndItemWithOwnerSerializer

class ProfileViewSet(viewsets.ModelViewSet):
    queryset = Profile.objects.all()
    serializer_class = ProfileSerializer
    
class ProfileAndItemsOwnedViewSet(viewsets.ReadOnlyModelViewSet):
    serializer_class = ProfileAndItemsOwnedSerializer
    
    def get_queryset(self):
        profile_id = self.kwargs['id']
        return Item.objects.filter(owner__id=profile_id)
    
class UsageAndItemWithOwnerViewSet(viewsets.ReadOnlyModelViewSet):
    serializer_class = UsageAndItemWithOwnerSerializer
    
    def get_queryset(self):
        profile_id = self.kwargs['id']
        return Usage.objects.filter(user__id=profile_id)
    
