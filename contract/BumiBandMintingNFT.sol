// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/IERC721.sol";
import "@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/interfaces/IERC2981.sol";

contract BumiBandMintingNFT is ERC721URIStorage, Ownable, IERC2981 {
    uint256 public nextTokenId;
    uint256 public creatorFeeBasisPoints;
    address public creator;
    address public payer;
    uint256 public totalCreatorFees;

    string private _name;
    string private _symbol;

    mapping(address => uint256[]) private _ownerTokens;
    mapping(uint256 => uint256) private _mintedTimestamps;

    constructor(
        string memory name_,
        string memory symbol_,
        address creator_,
        uint256 _creatorFeeBasisPoints,
        address payer_
    ) ERC721(name_, symbol_) Ownable(msg.sender) {
        _name = name_;
        _symbol = symbol_;
        payer = payer_;
        creatorFeeBasisPoints = _creatorFeeBasisPoints;
        creator = creator_;
        nextTokenId = 1;
    }

    modifier onlyPayer() {
        require(msg.sender == payer, "Caller is not the payer");
        _;
    }

    function setPayer(address newPayer) external onlyOwner {
        payer = newPayer;
    }

    function getPayer() public view returns (address) {
        return payer;
    }

    function royaltyInfo(uint256 , uint256 _salePrice) external view override returns (address receiver, uint256 royaltyAmount) {
        uint256 royalty = (_salePrice * creatorFeeBasisPoints) / 10000;
        return (creator, royalty);
    }

    function supportsInterface(bytes4 interfaceId) public view virtual override(ERC721URIStorage, IERC165) returns (bool) {
        return interfaceId == type(IERC2981).interfaceId || super.supportsInterface(interfaceId);
    }

    function mint(
        address to,
        string memory tokenURI_
    ) external onlyPayer {
        uint256 tokenId = nextTokenId;
        _safeMint(to, tokenId);
        _setTokenURI(tokenId, tokenURI_);
        _addTokenToOwnerEnumeration(to, tokenId);
        _mintedTimestamps[tokenId] = block.timestamp;

        nextTokenId++;
    }

    function mintToIndex(
        address to,
        string memory tokenURI_,
        uint256 index
    ) external onlyPayer {
        uint256 tokenId = index;
        _safeMint(to, tokenId);
        _setTokenURI(tokenId, tokenURI_);
        _addTokenToOwnerEnumeration(to, tokenId);
        _mintedTimestamps[tokenId] = block.timestamp;
    }

    function mintByOwner(
        address to,
        string memory tokenURI_
    ) external onlyOwner {
        uint256 tokenId = nextTokenId;
        _safeMint(to, tokenId);
        _setTokenURI(tokenId, tokenURI_);
        _addTokenToOwnerEnumeration(to, tokenId);
        _mintedTimestamps[tokenId] = block.timestamp;

        nextTokenId++;
    }

    function mintToIndexByOwner(
        address to,
        string memory tokenURI_,
        uint256 index
    ) external onlyOwner {
        uint256 tokenId = index;
        _safeMint(to, tokenId);
        _setTokenURI(tokenId, tokenURI_);
        _addTokenToOwnerEnumeration(to, tokenId);
        _mintedTimestamps[tokenId] = block.timestamp;
    }

    function getMintedTimestamp(uint256 tokenId) external view returns (uint256) {
        return _mintedTimestamps[tokenId];
    }

    function setCreatorFee(uint256 _creatorFeeBasisPoints) external onlyOwner {
        creatorFeeBasisPoints = _creatorFeeBasisPoints;
    }

    function setCreator(address _creator) external onlyOwner {
        creator = _creator;
    }

    function setName(string memory newName) external onlyOwner {
        _name = newName;
    }

    function setSymbol(string memory newSymbol) external onlyOwner {
        _symbol = newSymbol;
    }

    function name() public view override returns (string memory) {
        return _name;
    }

    function symbol() public view override returns (string memory) {
        return _symbol;
    }

    function tokenOfOwnerByIndex(address owner, uint256 index) public view returns (uint256) {
        require(index < _ownerTokens[owner].length, "Owner index out of bounds");
        return _ownerTokens[owner][index];
    }

    function tokensOfOwner(address owner) public view returns (uint256[] memory) {
        return _ownerTokens[owner];
    }

    function _removeTokenFromOwnerEnumeration(address from, uint256 tokenId) private {
        uint256 lastTokenIndex = _ownerTokens[from].length - 1;
        uint256 tokenIndex;

        for (uint256 i = 0; i < _ownerTokens[from].length; i++) {
            if (_ownerTokens[from][i] == tokenId) {
                tokenIndex = i;
                break;
            }
        }

        if (tokenIndex != lastTokenIndex) {
            uint256 lastTokenId = _ownerTokens[from][lastTokenIndex];
            _ownerTokens[from][tokenIndex] = lastTokenId;
        }

        _ownerTokens[from].pop();
    }

    function transferNFT(address from, address to, uint256 tokenId) external onlyOwner {
        require(ownerOf(tokenId) == from, "Transfer from incorrect owner");
        _transfer(from, to, tokenId);
        _removeTokenFromOwnerEnumeration(from, tokenId);
        _addTokenToOwnerEnumeration(to, tokenId);
    }

    function tokenURI(uint256 tokenId) public view override returns (string memory) {
        return super.tokenURI(tokenId);
    }

    function tokensOfOwnerByRange(address owner, uint256 start, uint256 end) public view returns (uint256[] memory) {
        require(start < end, "Invalid range");
        require(end <= _ownerTokens[owner].length, "Range out of bounds");
        uint256[] memory tokens = new uint256[](end - start);
        for (uint256 i = start; i < end; i++) {
            tokens[i - start] = _ownerTokens[owner][i];
        }
        return tokens;
    }

    function burn(uint256 tokenId) external onlyOwner {
        address owner = ownerOf(tokenId);
        _burn(tokenId);
        _removeTokenFromOwnerEnumeration(owner, tokenId);
    }

    receive() external payable {}

    function payCreatorFee(uint256 salePrice) private {
        require(salePrice > 0, "No ETH sent");
        uint256 fee = (salePrice * creatorFeeBasisPoints) / 10000;
        require(address(this).balance >= fee, "Insufficient balance for fee");
        totalCreatorFees += fee;
        payable(creator).transfer(fee);
    }

    function withdrawFees() external onlyOwner {
        uint256 balance = address(this).balance;
        require(balance > 0, "No fees available for withdrawal");
        payable(creator).transfer(balance);
    }

    function getCreatorFeeBasisPoints() external view returns (uint256) {
        return creatorFeeBasisPoints;
    }

    function secondaryTransfer(address from, address to, uint256 tokenId, uint256 salePrice) external {
        require(ownerOf(tokenId) == from, "Transfer from incorrect owner");
        require(salePrice > 0, "Sale price must be greater than zero");

        uint256 royalty = (salePrice * creatorFeeBasisPoints) / 10000;
        uint256 sellerAmount = salePrice - royalty;

        require(address(this).balance >= royalty, "Insufficient balance for royalty");

        payable(creator).transfer(royalty);
        totalCreatorFees += royalty;

        payable(from).transfer(sellerAmount);

        _transfer(from, to, tokenId);
        _removeTokenFromOwnerEnumeration(from, tokenId);
        _addTokenToOwnerEnumeration(to, tokenId);
    }

    function updateTokenURI(uint256 tokenId, string memory newTokenURI) external onlyOwner {
        _setTokenURI(tokenId, newTokenURI);
    }

    function _updateOwnerTokens(address from, address to, uint256 tokenId) private {
        if (from != address(0)) {
            _removeTokenFromOwnerEnumeration(from, tokenId);
        }
        if (to != address(0)) {
            _addTokenToOwnerEnumeration(to, tokenId);
        }
    }

    function _addTokenToOwnerEnumeration(address to, uint256 tokenId) private {
        _ownerTokens[to].push(tokenId);
    }

    function transferFrom(address from, address to, uint256 tokenId) public override(ERC721, IERC721) {
        super.transferFrom(from, to, tokenId);
        _updateOwnerTokens(from, to, tokenId);
    }

    function safeTransferFrom(address from, address to, uint256 tokenId, bytes memory data) public override(ERC721, IERC721) {
        super.safeTransferFrom(from, to, tokenId, data);
        _updateOwnerTokens(from, to, tokenId);
    }
}
