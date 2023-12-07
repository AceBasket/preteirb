from rest_framework import serializers
from .models import Profile
from items.serializers import ItemSerializer

class ProfileSerializer(serializers.ModelSerializer):
    class Meta:
        model = Profile
        fields = '__all__'
        
class ProfileAndItemsOwnedSerializer(serializers.ModelSerializer):
    items_owned = ItemSerializer(many=True, read_only=True)
    class Meta:
        model = Profile
        fields = '__all__'