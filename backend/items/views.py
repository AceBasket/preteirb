from django.shortcuts import render
from rest_framework import viewsets
from .serializers import ItemSerializer, ItemAndUsagesSerializer
from .models import Item

class ItemViewSet(viewsets.ModelViewSet):
    queryset = Item.objects.all()
    serializer_class = ItemSerializer
    
    def list(self, request):
        queryset = request.user.items.all()
        serializer = ItemSerializer(queryset, many=True)
        return Response(serializer.data)
    
class ItemAndUsagesViewSet(viewsets.ReadOnlyModelViewSet):
    queryset = Item.objects.all()
    serializer_class = ItemAndUsagesSerializer