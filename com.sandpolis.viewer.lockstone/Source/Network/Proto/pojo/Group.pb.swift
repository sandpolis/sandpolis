//****************************************************************************//
//                                                                            //
//                Copyright © 2015 - 2019 Subterranean Security               //
//                                                                            //
//  Licensed under the Apache License, Version 2.0 (the "License");           //
//  you may not use this file except in compliance with the License.          //
//  You may obtain a copy of the License at                                   //
//                                                                            //
//      http://www.apache.org/licenses/LICENSE-2.0                            //
//                                                                            //
//  Unless required by applicable law or agreed to in writing, software       //
//  distributed under the License is distributed on an "AS IS" BASIS,         //
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  //
//  See the License for the specific language governing permissions and       //
//  limitations under the License.                                            //
//                                                                            //
//****************************************************************************//
import Foundation
import SwiftProtobuf

// If the compiler emits an error on this type, it is because this file
// was generated by a version of the `protoc` Swift plug-in that is
// incompatible with the version of SwiftProtobuf to which you are linking.
// Please ensure that your are building against the same version of the API
// that was used to generate this file.
fileprivate struct _GeneratedWithProtocGenSwiftVersion: SwiftProtobuf.ProtobufAPIVersionCheck {
  struct _2: SwiftProtobuf.ProtobufAPIVersion_2 {}
  typealias Version = _2
}

///*
/// The configuration of a Group.
struct Pojo_GroupConfig {
  // SwiftProtobuf.Message conformance is added in an extension below. See the
  // `Message` and `Message+*Additions` files in the SwiftProtobuf library for
  // methods supported on all messages.

  /// The primary ID
  var id: String {
    get {return _id ?? String()}
    set {_id = newValue}
  }
  /// Returns true if `id` has been explicitly set.
  var hasID: Bool {return self._id != nil}
  /// Clears the value of `id`. Subsequent reads from it will return its default value.
  mutating func clearID() {self._id = nil}

  /// The user-friendly name
  var name: String {
    get {return _name ?? String()}
    set {_name = newValue}
  }
  /// Returns true if `name` has been explicitly set.
  var hasName: Bool {return self._name != nil}
  /// Clears the value of `name`. Subsequent reads from it will return its default value.
  mutating func clearName() {self._name = nil}

  /// The username of the user that owns this group
  var owner: String {
    get {return _owner ?? String()}
    set {_owner = newValue}
  }
  /// Returns true if `owner` has been explicitly set.
  var hasOwner: Bool {return self._owner != nil}
  /// Clears the value of `owner`. Subsequent reads from it will return its default value.
  mutating func clearOwner() {self._owner = nil}

  /// The usernames of all group members
  var member: [String] = []

  /// The password mechanisms that belong to the group
  var passwordMechanism: [Util_PasswordContainer] = []

  /// The key mechanisms that belong to the group
  var keyMechanism: [Util_KeyContainer] = []

  var unknownFields = SwiftProtobuf.UnknownStorage()

  init() {}

  fileprivate var _id: String? = nil
  fileprivate var _name: String? = nil
  fileprivate var _owner: String? = nil
}

///*
/// Group statistics.
struct Pojo_GroupStats {
  // SwiftProtobuf.Message conformance is added in an extension below. See the
  // `Message` and `Message+*Additions` files in the SwiftProtobuf library for
  // methods supported on all messages.

  /// The creation timestamp
  var ctime: Int64 {
    get {return _ctime ?? 0}
    set {_ctime = newValue}
  }
  /// Returns true if `ctime` has been explicitly set.
  var hasCtime: Bool {return self._ctime != nil}
  /// Clears the value of `ctime`. Subsequent reads from it will return its default value.
  mutating func clearCtime() {self._ctime = nil}

  /// The modification timestamp
  var mtime: Int64 {
    get {return _mtime ?? 0}
    set {_mtime = newValue}
  }
  /// Returns true if `mtime` has been explicitly set.
  var hasMtime: Bool {return self._mtime != nil}
  /// Clears the value of `mtime`. Subsequent reads from it will return its default value.
  mutating func clearMtime() {self._mtime = nil}

  var unknownFields = SwiftProtobuf.UnknownStorage()

  init() {}

  fileprivate var _ctime: Int64? = nil
  fileprivate var _mtime: Int64? = nil
}

///*
/// A Group container.
struct Pojo_ProtoGroup {
  // SwiftProtobuf.Message conformance is added in an extension below. See the
  // `Message` and `Message+*Additions` files in the SwiftProtobuf library for
  // methods supported on all messages.

  var config: Pojo_GroupConfig {
    get {return _storage._config ?? Pojo_GroupConfig()}
    set {_uniqueStorage()._config = newValue}
  }
  /// Returns true if `config` has been explicitly set.
  var hasConfig: Bool {return _storage._config != nil}
  /// Clears the value of `config`. Subsequent reads from it will return its default value.
  mutating func clearConfig() {_uniqueStorage()._config = nil}

  var stats: Pojo_GroupStats {
    get {return _storage._stats ?? Pojo_GroupStats()}
    set {_uniqueStorage()._stats = newValue}
  }
  /// Returns true if `stats` has been explicitly set.
  var hasStats: Bool {return _storage._stats != nil}
  /// Clears the value of `stats`. Subsequent reads from it will return its default value.
  mutating func clearStats() {_uniqueStorage()._stats = nil}

  var unknownFields = SwiftProtobuf.UnknownStorage()

  init() {}

  fileprivate var _storage = _StorageClass.defaultInstance
}

// MARK: - Code below here is support for the SwiftProtobuf runtime.

fileprivate let _protobuf_package = "pojo"

extension Pojo_GroupConfig: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  static let protoMessageName: String = _protobuf_package + ".GroupConfig"
  static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .same(proto: "id"),
    2: .same(proto: "name"),
    3: .same(proto: "owner"),
    4: .same(proto: "member"),
    5: .standard(proto: "password_mechanism"),
    6: .standard(proto: "key_mechanism"),
  ]

  mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    while let fieldNumber = try decoder.nextFieldNumber() {
      switch fieldNumber {
      case 1: try decoder.decodeSingularStringField(value: &self._id)
      case 2: try decoder.decodeSingularStringField(value: &self._name)
      case 3: try decoder.decodeSingularStringField(value: &self._owner)
      case 4: try decoder.decodeRepeatedStringField(value: &self.member)
      case 5: try decoder.decodeRepeatedMessageField(value: &self.passwordMechanism)
      case 6: try decoder.decodeRepeatedMessageField(value: &self.keyMechanism)
      default: break
      }
    }
  }

  func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    if let v = self._id {
      try visitor.visitSingularStringField(value: v, fieldNumber: 1)
    }
    if let v = self._name {
      try visitor.visitSingularStringField(value: v, fieldNumber: 2)
    }
    if let v = self._owner {
      try visitor.visitSingularStringField(value: v, fieldNumber: 3)
    }
    if !self.member.isEmpty {
      try visitor.visitRepeatedStringField(value: self.member, fieldNumber: 4)
    }
    if !self.passwordMechanism.isEmpty {
      try visitor.visitRepeatedMessageField(value: self.passwordMechanism, fieldNumber: 5)
    }
    if !self.keyMechanism.isEmpty {
      try visitor.visitRepeatedMessageField(value: self.keyMechanism, fieldNumber: 6)
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  static func ==(lhs: Pojo_GroupConfig, rhs: Pojo_GroupConfig) -> Bool {
    if lhs._id != rhs._id {return false}
    if lhs._name != rhs._name {return false}
    if lhs._owner != rhs._owner {return false}
    if lhs.member != rhs.member {return false}
    if lhs.passwordMechanism != rhs.passwordMechanism {return false}
    if lhs.keyMechanism != rhs.keyMechanism {return false}
    if lhs.unknownFields != rhs.unknownFields {return false}
    return true
  }
}

extension Pojo_GroupStats: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  static let protoMessageName: String = _protobuf_package + ".GroupStats"
  static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .same(proto: "ctime"),
    2: .same(proto: "mtime"),
  ]

  mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    while let fieldNumber = try decoder.nextFieldNumber() {
      switch fieldNumber {
      case 1: try decoder.decodeSingularInt64Field(value: &self._ctime)
      case 2: try decoder.decodeSingularInt64Field(value: &self._mtime)
      default: break
      }
    }
  }

  func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    if let v = self._ctime {
      try visitor.visitSingularInt64Field(value: v, fieldNumber: 1)
    }
    if let v = self._mtime {
      try visitor.visitSingularInt64Field(value: v, fieldNumber: 2)
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  static func ==(lhs: Pojo_GroupStats, rhs: Pojo_GroupStats) -> Bool {
    if lhs._ctime != rhs._ctime {return false}
    if lhs._mtime != rhs._mtime {return false}
    if lhs.unknownFields != rhs.unknownFields {return false}
    return true
  }
}

extension Pojo_ProtoGroup: SwiftProtobuf.Message, SwiftProtobuf._MessageImplementationBase, SwiftProtobuf._ProtoNameProviding {
  static let protoMessageName: String = _protobuf_package + ".ProtoGroup"
  static let _protobuf_nameMap: SwiftProtobuf._NameMap = [
    1: .same(proto: "config"),
    2: .same(proto: "stats"),
  ]

  fileprivate class _StorageClass {
    var _config: Pojo_GroupConfig? = nil
    var _stats: Pojo_GroupStats? = nil

    static let defaultInstance = _StorageClass()

    private init() {}

    init(copying source: _StorageClass) {
      _config = source._config
      _stats = source._stats
    }
  }

  fileprivate mutating func _uniqueStorage() -> _StorageClass {
    if !isKnownUniquelyReferenced(&_storage) {
      _storage = _StorageClass(copying: _storage)
    }
    return _storage
  }

  mutating func decodeMessage<D: SwiftProtobuf.Decoder>(decoder: inout D) throws {
    _ = _uniqueStorage()
    try withExtendedLifetime(_storage) { (_storage: _StorageClass) in
      while let fieldNumber = try decoder.nextFieldNumber() {
        switch fieldNumber {
        case 1: try decoder.decodeSingularMessageField(value: &_storage._config)
        case 2: try decoder.decodeSingularMessageField(value: &_storage._stats)
        default: break
        }
      }
    }
  }

  func traverse<V: SwiftProtobuf.Visitor>(visitor: inout V) throws {
    try withExtendedLifetime(_storage) { (_storage: _StorageClass) in
      if let v = _storage._config {
        try visitor.visitSingularMessageField(value: v, fieldNumber: 1)
      }
      if let v = _storage._stats {
        try visitor.visitSingularMessageField(value: v, fieldNumber: 2)
      }
    }
    try unknownFields.traverse(visitor: &visitor)
  }

  static func ==(lhs: Pojo_ProtoGroup, rhs: Pojo_ProtoGroup) -> Bool {
    if lhs._storage !== rhs._storage {
      let storagesAreEqual: Bool = withExtendedLifetime((lhs._storage, rhs._storage)) { (_args: (_StorageClass, _StorageClass)) in
        let _storage = _args.0
        let rhs_storage = _args.1
        if _storage._config != rhs_storage._config {return false}
        if _storage._stats != rhs_storage._stats {return false}
        return true
      }
      if !storagesAreEqual {return false}
    }
    if lhs.unknownFields != rhs.unknownFields {return false}
    return true
  }
}
