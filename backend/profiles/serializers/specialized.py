from rest_framework import serializers
from items.serializers.common import ItemSerializer 
from ..models import Profile     

class ProfileAndItemsOwnedSerializer(serializers.ModelSerializer):
    items_owned = ItemSerializer(many=True, read_only=True)
    class Meta:
        model = Profile
        fields = '__all__'