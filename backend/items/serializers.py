from rest_framework import serializers
from .models import Item
from usages.serializers import UsageSerializer

class ItemSerializer(serializers.ModelSerializer):
    class Meta:
        model = Item
        fields = '__all__'
        
class ItemAndUsagesSerializer(serializers.ModelSerializer):
    usages = UsageSerializer(many=True, read_only=True)
    class Meta:
        model = Item
        fields = '__all__'