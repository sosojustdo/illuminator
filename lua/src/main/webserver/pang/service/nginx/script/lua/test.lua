--
-- Created by IntelliJ IDEA.
-- User: stevexu
-- Date: 3/18/17
-- Time: 10:25 PM
-- To change this template use File | Settings | File Templates.
--

local iresty_test    = require "resty.iresty_test"
local unit = iresty_test.new({unit_name="buyboxapi"})
local cjson = require "cjson"
local util = require "util"

function unit:init(  )
    self:log("test begins")
end


function unit:testDeduplicate()
    local winners = {}
    local winner1 = {}
    winner1["vendorItemId"] = "3000000101"
    winner1["vendorId"] = "A00010028"
    winner1["itemid"] = "1105"
    local winner2 = {}
    winner2["vendorItemId"] = "3000000102"
    winner2["vendorId"] = "A00025376"
    winner2["itemid"] = "1105"
    local winner3 = {}
    winner3["vendorItemId"] = "3000000103"
    winner3["vendorId"] = "A00038294"
    winner3["itemid"] = "1105"
    local winner4 = {}
    winner4["vendorItemId"] = "3000000104"
    winner4["vendorId"] = "A00025376"
    winner4["itemid"] = "1105"
    local winner5 = {}
    winner5["vendorItemId"] = "3000000105"
    winner5["vendorId"] = "A00045899"
    winner5["itemid"] = "1105"
    table.insert(winners, winner1)
    table.insert(winners, winner2)
    table.insert(winners, winner3)
    table.insert(winners, winner4)
    table.insert(winners, winner5)
    local dedupWinners = util.deduplicate(winners)
    if (#dedupWinners == 2) and ("3000000101" == dedupWinners[1]["vendorItemId"]) and ("3000000102" == dedupWinners[2]["vendorItemId"]) then
        self:log("vendor deduplication test successfully")
    else
        error("vendor deduplication test failed")
    end
end

function unit:testSplit(  )
    local itemIds = "200,201,202"
    local splitArray = util.split(itemIds,",")
    if (#splitArray == 3) and ("200" == splitArray[1]) and ("201" == splitArray[2]) and  ("202" == splitArray[3]) then
        self:log("split utility test successfully")
    else
        error("split utility test failed")
    end
end

-- units test
unit:run()