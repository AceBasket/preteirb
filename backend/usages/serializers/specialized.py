from rest_framework import serializers
from items.serializers.common import ItemSerializer
from profiles.serializers.common import ProfileSerializer
from ..models import Usage
             
class UsageAndItemWithOwnerSerializer(serializers.ModelSerializer):    
    item = ItemSerializer()
    item__owner = ProfileSerializer()
    class Meta:
        model = Usage
        fields = '__all__'