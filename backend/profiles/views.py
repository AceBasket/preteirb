from django.shortcuts import render
from rest_framework import viewsets
from .models import Profile
from .serializers import ProfileSerializer, ProfileAndItemsOwnedSerializer

class ProfileViewSet(viewsets.ModelViewSet):
    queryset = Profile.objects.all()
    serializer_class = ProfileSerializer
    
class ProfileAndItemsOwnedViewSet(viewsets.ReadOnlyModelViewSet):
    queryset = Profile.objects.all()
    serializer_class = ProfileAndItemsOwnedSerializer
