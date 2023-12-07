from django.db import models
from profiles.models import Profile
from items.models import Item

class Usage(models.Model):
    user = models.ForeignKey(Profile, on_delete=models.CASCADE, related_name='usages')
    item = models.ForeignKey(Item, on_delete=models.CASCADE, related_name='usages')
    start = models.DateField()
    end = models.DateField()
