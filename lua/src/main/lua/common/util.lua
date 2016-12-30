--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 12/29/16
-- Time: 8:53 PM
-- To change this template use File | Settings | File Templates.
--
local _M = {}
_M._VERSION = '0.1'

function _M.split(str, separator)
    local splitArray = {}
    if(string.len(str)<1) then
        return splitArray
    end
    local startIndex = 1
    local splitIndex = 1
    while true do
        local lastIndex = string.find(str, separator, startIndex)
        if not lastIndex then
            splitArray[splitIndex] = string.sub(str, startIndex, string.len(str))
            break
        end
        splitArray[splitIndex] = string.sub(str, startIndex, lastIndex - 1)
        startIndex = lastIndex + string.len(separator)
        splitIndex = splitIndex + 1
    end
    return splitArray
end

math.randomseed(os.time())

local function swap(array, index1, index2)
    array[index1], array[index2] = array[index2], array[index1]
end

local function shuffleArray(array)
    local counter = #array
    while counter > 1 do
        local index = math.random(counter)
        swap(array,index,counter)
        counter = counter - 1
    end
end

function _M.shuffle(tmp)
    local shuffledWinners = {}
    for rank,val in pairs(tmp) do
        if(#val <= 1) then
            --no need to shuffle
        else
            --there is multiple vendoritem within same rank, need to shuffle
            shuffleArray(val)
        end
        for v=1, #val do
            table.insert(shuffledWinners, val[v])
        end
    end
    return shuffledWinners
end

function _M.deduplicate(shuffledWinners)
    local encounter = false
    local vendorSet = {}
    local dedupWinners  ={}
    for s=1, #shuffledWinners do
        if shuffledWinners[s]["crv"] then
            --only need one vendoritem for consignment retail vendors
            if not encounter then
                --we don't need to display if it is consignment retail vendors
                shuffledWinners[s]["crv"] = nil
                table.insert(dedupWinners, shuffledWinners[s])
                encounter = true
            end
        else
            --only need one vendoritem for each vendor, for other vendoritem within same vendor, just ignore it
            local vendor = shuffledWinners[s]["vendorId"]
            if not vendorSet[vendor] then
                --we don't need to display if it is consignment retail vendors
                shuffledWinners[s]["crv"] = nil
                table.insert(dedupWinners, shuffledWinners[s])
                vendorSet[vendor] = true
            end
        end
    end
    return dedupWinners
end

return _M