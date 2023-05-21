from collections import Counter
from copy import copy

input()
costs = [int(c) for c in input().split()]
input()
orders = [int(o) for o in input().split()]

maxOrder = max(orders)

priceToOrders = {}

def addOrdersToSum(sum, cost, costIndex):
    global priceToOrders

    tempCounter = copy(priceToOrders[sum - cost])
    tempCounter[costIndex] += 1

    if(sum in priceToOrders and tempCounter != priceToOrders[sum]):
        priceToOrders[sum] = "Ambiguous"
    else:
        priceToOrders[sum] = copy(priceToOrders[sum - cost])
        priceToOrders[sum][costIndex] += 1

for currentSum in range(maxOrder + 1):
    for currentCostIndex, currentCost in enumerate(costs):
        if(currentSum >= currentCost):
            if((currentSum - currentCost) in priceToOrders and priceToOrders[currentSum - currentCost] == "Ambiguous"):
                priceToOrders[currentSum] = "Ambiguous"
            elif((currentSum - currentCost) in priceToOrders):
                addOrdersToSum(currentSum, currentCost, currentCostIndex)
            elif(currentCost == currentSum):
                if(currentSum in priceToOrders):
                    priceToOrders[currentSum] = "Ambiguous"
                else:
                    if(currentSum not in priceToOrders):
                        priceToOrders[currentSum] = Counter()
                    priceToOrders[currentSum][currentCostIndex] += 1

for order in orders:
    if(order in priceToOrders and priceToOrders[order] == "Ambiguous"):
        print("Ambiguous")
    elif(order not in priceToOrders):
        print("Impossible")
    else:
        for menuItemIndex in sorted(priceToOrders[order]):
            for _ in range(priceToOrders[order][menuItemIndex]):
                print(menuItemIndex + 1, end=" ")
        print()