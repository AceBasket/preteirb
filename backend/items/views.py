from django.shortcuts import render
from rest_framework import viewsets
from .serializers.common import ItemSerializer
from .serializers.specialized import ItemAndUsagesSerializer
from .models import Item

class ItemViewSet(viewsets.ModelViewSet):
    queryset = Item.objects.all()
    serializer_class = ItemSerializer
    
    def list(self, request):
        queryset = request.user.items.all()
        serializer = ItemSerializer(queryset, many=True)
        return Response(serializer.data)
    
class ItemAndUsagesViewSet(viewsets.ReadOnlyModelViewSet):
    serializer_class = ItemAndUsagesSerializer
    
    def get_queryset(self):
        item_id = self.kwargs['id']
        return Item.objects.filter(id=item_id)