from django.shortcuts import render
from rest_framework import viewsets
from .models import Usage
from .serializers.common import UsageSerializer
from .serializers.specialized import UsageAndItemWithOwnerSerializer

class UsageViewSet(viewsets.ModelViewSet):
    queryset = Usage.objects.all()
    serializer_class = UsageSerializer
    

